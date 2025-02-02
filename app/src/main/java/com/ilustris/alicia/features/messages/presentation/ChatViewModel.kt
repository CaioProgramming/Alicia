package com.ilustris.alicia.features.messages.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.ai.callback.CallbackExecutor
import com.ilustris.alicia.ai.inputs.InputGenerator
import com.ilustris.alicia.ai.message.MessageGenerator
import com.ilustris.alicia.ai.model.PromptBuilder
import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.ai.model.Prompts
import com.ilustris.alicia.ai.model.ai.AIResponse
import com.ilustris.alicia.ai.model.buildPrompt
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Sender
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.MessageGroup
import com.ilustris.alicia.features.messages.domain.usecase.ChatDataManager
import com.ilustris.alicia.features.messages.domain.usecase.ChatUseCase
import com.ilustris.alicia.features.user.data.model.User
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit
import java.util.Calendar
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ChatViewModel
    @Inject
    constructor(
        private val userUseCase: UserUseCase,
        private val chatUseCase: ChatUseCase,
        private val messageGenerator: MessageGenerator,
        private val callbackExecutor: CallbackExecutor,
        private val inputGenerator: InputGenerator,
        private val chatDataManager: ChatDataManager,
    ) : ViewModel() {
        fun startChat() {
            getUser()
            observeMessages()
            generateSuggestions()
        }

        private fun observeMessages() {
            viewModelScope.launch(Dispatchers.IO) {
                chatUseCase.getMessages().collect {
                    Log.d(javaClass.simpleName, "current Messages: $it")
                    this@ChatViewModel.messages.emit(it)
                }
            }
        }

        val humor = PromptConfig.HumorConfig()
        val messages = MutableStateFlow<List<MessageGroup>>(emptyList())
        val suggestions = MutableStateFlow<List<String>>(emptyList())
        val user = MutableStateFlow<User?>(null)
        val state = MutableStateFlow<ChatState?>(ChatState.Loading)

        fun launchAction(homeAction: ChatAction) {
            state.value = ChatState.Loading

            when (homeAction) {
                is ChatAction.SendMessage -> processMessage(homeAction.message)
                ChatAction.FetchUser -> getUser()
            }
        }

        private fun setToIdle() {
            viewModelScope.launch(Dispatchers.IO) {
                state.emit(ChatState.Idle)
            }
        }

        private fun sendError(text: String? = null) {
            viewModelScope.launch(Dispatchers.IO) {
                state.emit(ChatState.Error(text))

                delay(10.seconds)

                state.emit(ChatState.Idle)
            }
        }

        private fun saveAIMessage(
            aiResponse: AIResponse,
            extraKey: String? = null,
            onComplete: ((Message) -> Unit)? = null,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                chatUseCase
                    .saveGeneratedMessage(aiResponse, extraKey)
                    .onSuccess {
                        onComplete?.invoke(it)
                        setToIdle()
                    }.onFailure {
                        sendError("Erro ao salvar mensagem :(")
                    }
            }
        }

        private fun generateMessage(
            promptBuilder: PromptBuilder,
            extraKey: String? = null,
            useTypes: Boolean,
            errorMessage: String? = null,
            onComplete: ((Message) -> Unit)? = null,
            onError: () -> Unit = { sendError() },
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                messageGenerator
                    .generateMessage(
                        promptBuilder.build(),
                        useTypes,
                    )?.let {
                        saveAIMessage(it, extraKey, onComplete)
                    } ?: {
                    onError()
                }
                setToIdle()
            }
        }

        private fun generateSuggestions() {
            viewModelScope.launch(Dispatchers.IO) {
                if (user.value == null) return@launch
                val inputs = inputGenerator.generateInputs()
                suggestions.emit(inputs)
            }
        }

        private fun handleCallBackSuccess(
            value: String,
            useTypes: Boolean,
            isUser: Boolean = false,
            extraKey: String?,
            onComplete: ((Message) -> Unit)? = null,
        ) {
            generateMessage(
                buildPrompt {
                    if (!isUser) {
                        addPrompt(PromptConfig.CallBackSuccessConfig(value).description)
                    } else {
                        addPrompt(Prompts.Greeting.prompt.replace("[username]", value))
                    }
                    addPrompt(humor.description)
                },
                extraKey = extraKey,
                onComplete = onComplete,
                useTypes = useTypes,
                onError = {
                    generateMessage(
                        buildPrompt {
                            addPrompt(
                                PromptConfig
                                    .ErrorExplanation(
                                        "An error ocurred while generating a message," +
                                            " but the data was saved successfully",
                                    ).description,
                            )
                            addPrompt(humor.description)
                        },
                        useTypes = false,
                    )
                },
            )
        }

        private fun processMessage(message: String) {
            viewModelScope.launch(Dispatchers.IO) {
                val userMessage =
                    Message(
                        text = message,
                        sender = Sender.USER,
                        sentTime = Calendar.getInstance().timeInMillis,
                    )
                chatUseCase.saveMessage(
                    userMessage,
                )

                user.value?.let {
                    replyMessage(userMessage, it)
                } ?: run {
                    executeCallback(userMessage)
                }
            }
        }

        private fun replyMessage(
            userMessage: Message,
            it: User,
        ) {
            generateMessage(
                buildPrompt {
                    addPrompt(PromptConfig.ReplyConfig(userMessage.text, it.name).description)
                    addPrompt(humor.description)
                },
                useTypes = false,
                onComplete = { executeCallback(userMessage) },
            )
        }

        private fun executeCallback(message: Message) {
            viewModelScope.launch {
                val callBack = callbackExecutor.execute(message.text)
                if (callBack == null) {
                    sendError("Erro ao salvar as informações, vamos tentar novamente.")
                    return@launch
                } else {
                    saveCallbackResult(callBack)
                }
            }
        }

        private fun saveCallbackResult(callBack: Pair<Action, Any>) {
            viewModelScope.launch {
                chatDataManager
                    .saveData(callBack)
                    .onSuccess {
                        handleCallBackSuccess(
                            callBack.second.toString(),
                            isUser = callBack.first == Action.NAME,
                            useTypes = callBack.first != Action.NAME,
                            extraKey = it.toString(),
                        ) {
                            generateSuggestions()
                        }
                    }.onFailure {
                        sendError("Erro ao salvar as informações, vamos tentar novamente.")
                    }
            }
        }

        private fun shouldSendNewMessage(lastMessage: Message?): Boolean {
            if (lastMessage == null) return true
            val todayDate = Calendar.getInstance()
            val lastMessageDate =
                Calendar.getInstance().apply {
                    timeInMillis = lastMessage.sentTime
                }
            return ChronoUnit.DAYS.between(lastMessageDate.toInstant(), todayDate.toInstant()) > 0
        }

        private fun getUser() {
            viewModelScope.launch(Dispatchers.IO) {
                val currentUser = userUseCase.getUserByIdAsync()
                val lastMessage = chatUseCase.getLastMessage()

                if (currentUser == null) {
                    if (shouldSendNewMessage(lastMessage)) {
                        generateMessage(
                            buildPrompt {
                                addPrompt(PromptConfig.AIntroduction.description)
                            },
                            useTypes = false,
                            onComplete = {
                                generateMessage(
                                    buildPrompt {
                                        addPrompt(PromptConfig.NameConfig.description)
                                    },
                                    useTypes = false,
                                )
                            },
                        )
                    }
                } else {
                    if (shouldSendNewMessage(lastMessage)) {
                        val prompt = Prompts.Greeting.prompt.replace("[username]", currentUser.name)
                        generateMessage(
                            buildPrompt {
                                addPrompt(prompt)
                                addPrompt(humor.description)
                            },
                            useTypes = false,
                        )
                    }
                    lastMessage?.let { generateSuggestions() }
                    user.emit(currentUser)
                }
                setToIdle()
            }
        }
    }
