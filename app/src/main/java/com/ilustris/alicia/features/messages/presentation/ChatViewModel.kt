package com.ilustris.alicia.features.messages.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.ai.model.PromptBuilder
import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.ai.model.Prompts
import com.ilustris.alicia.ai.model.ai.AICallBack
import com.ilustris.alicia.ai.model.ai.AIResponse
import com.ilustris.alicia.ai.model.ai.AISuggestions
import com.ilustris.alicia.ai.model.buildPrompt
import com.ilustris.alicia.ai.usecase.AIUseCase
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.data.model.TagHelper
import com.ilustris.alicia.features.finnance.data.model.findTag
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Sender
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.MessageGroup
import com.ilustris.alicia.features.messages.domain.model.NameBody
import com.ilustris.alicia.features.messages.domain.model.bodyClass
import com.ilustris.alicia.features.messages.domain.usecase.ChatUseCase
import com.ilustris.alicia.features.user.data.model.User
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import com.ilustris.alicia.utils.containsNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.lastOrNull
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
        private val aiUseCase: AIUseCase,
        private val financeUseCase: FinanceUseCase,
    ) : ViewModel() {
        fun startChat() {
            getUser()
            observeGoals()
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

        private fun observeGoals() =
            viewModelScope.launch(Dispatchers.IO) {
                financeUseCase.getGoals().collect { goals ->
                    goals.forEach { goal ->
                        val ammount = financeUseCase.getAmount().lastOrNull() ?: 0.0
                        if (goal.isComplete.not() && ammount >= goal.value) {
                            val today = Calendar.getInstance()
                            val goalDate =
                                Calendar.getInstance().apply { timeInMillis = goal.createdAt }
                            if (today[Calendar.DAY_OF_YEAR] == goalDate[Calendar.DAY_OF_YEAR]) {
                                completeGoal(goal)
                            }
                        }
                    }
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
                        sendError("Erro ao salvar mensagem :(, vamos tentar novamente.")
                    }
            }
        }

        private fun generateMessage(
            promptBuilder: PromptBuilder,
            extraKey: String? = null,
            useTypes: Boolean,
            errorMessage: String? = null,
            onComplete: ((Message) -> Unit)? = null,
            onError: () -> Unit = {},
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                val typeReplacement = if (useTypes) Type.entries.joinToString(("|")) else "null"
                aiUseCase
                    .generateResponse(
                        promptBuilder,
                        AIResponse::class.java,
                        specificReplacement = Pair("type", typeReplacement),
                    ).onSuccess {
                        saveAIMessage(it, extraKey, onComplete)
                        setToIdle()
                    }.onFailure {
                        sendError(errorMessage)
                        onError()
                    }
            }
        }

        private fun generateSuggestions() {
            viewModelScope.launch(Dispatchers.IO) {
                if (user.value == null) return@launch
                aiUseCase
                    .generateResponse(
                        buildPrompt {
                            addPrompt(PromptConfig.SuggestionsConfig.description)
                        },
                        AISuggestions::class.java,
                        specificReplacement = Pair("suggestions", "List<String>"),
                    ).onSuccess {
                        suggestions.value = it.suggestions
                    }.onFailure {
                        sendError("Erro ao processar sugestões :(, vamos tentar novamente.")
                    }
            }
        }

        private fun saveUser(userBody: NameBody) {
            viewModelScope.launch(Dispatchers.IO) {
                if (userBody.name.containsNull()) {
                    sendError("Erro ao salvar usuário vamos tentar novamente :(")
                }
                userUseCase.saveUser(userBody.name)

                handleCallBackSuccess(userBody.name, false, isUser = true, extraKey = null) {
                    generateMessage(
                        buildPrompt {
                            addPrompt(PromptConfig.FeaturesExamples.description)
                            addPrompt(humor.description)
                        },
                        useTypes = false,
                        onComplete = {
                            generateSuggestions()
                        },
                    )
                }
                val newUser = userUseCase.getUserByIdAsync()
                user.emit(newUser)
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
                onComplete = {
                    executeCallback(
                        userMessage,
                    )
                },
            )
        }

        private fun executeCallback(message: Message) {
            viewModelScope.launch {
                aiUseCase
                    .generateResponse(
                        buildPrompt {
                            addPrompt(PromptConfig.CallBackConfig(message.text).description)
                            addPrompt(PromptConfig.ActionConfig.description)
                        },
                        AICallBack::class.java,
                        requireTranslation = false,
                        useContext = false,
                    ).onSuccess {
                        handleCallBack(it, message)
                    }.onFailure {
                        sendError("Erro ao processar mensagem :(, vamos tentar novamente.")
                    }
            }
        }

        private fun handleCallBack(
            callBack: AICallBack,
            supportMessage: Message? = null,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                aiUseCase
                    .generateResponse(
                        buildPrompt {
                            supportMessage?.let {
                                addPrompt(PromptConfig.MessageResourceConfig(it.text).description)
                            }
                            addPrompt(PromptConfig.FormatResponseConfig(callBack.value).description)
                            addPrompt(PromptConfig.ExtractValuableDataConfig.description)
                        },
                        callBack.action.bodyClass(),
                        requireTranslation = false,
                        specificReplacement = Pair("tag", Tag.entries.joinToString("|")),
                    ).onSuccess {
                        if (it == null) {
                            sendError("Erro ao processar mensagem :(, vamos tentar novamente.")
                            return@onSuccess
                        }
                        when (callBack.action) {
                            Action.NAME -> {
                                saveUser(it as NameBody)
                            }

                            Action.PROFIT, Action.LOSS ->
                                saveMovimentation(
                                    it as Movimentation,
                                    callBack.action,
                                )

                            Action.GOAL -> saveGoal(it as Goal)
                            Action.NONE, Action.BALANCE ->
                                saveAIMessage(
                                    it as AIResponse,
                                )
                        }
                    }.onFailure {
                        sendError("Erro ao processar mensagem :(, vamos tentar novamente.")
                    }
            }
        }

        private fun completeGoal(goal: Goal) {
            viewModelScope.launch(Dispatchers.IO) {
                financeUseCase.updateGoal(
                    goal.copy(
                        isComplete = true,
                        completedAt = Calendar.getInstance().timeInMillis,
                    ),
                )
            }
        }

        private fun saveGoal(goal: Goal) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val formattedGoal =
                        goal.copy(
                            id = 0,
                            createdAt = Calendar.getInstance().timeInMillis,
                            badge = TagHelper.getRandomBadgeForTag(goal.tag.findTag()),
                        )
                    val newGoal = financeUseCase.saveGoal(formattedGoal)

                    handleCallBackSuccess(
                        goal.toString(),
                        true,
                        extraKey = newGoal.toString(),
                        onComplete = {
                            generateSuggestions()
                        },
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    sendError("Ocorreu um erro ao salvar as informações, vamos tentar novamente.")
                }
            }
        }

        private fun saveMovimentation(
            movimentation: Movimentation,
            action: Action,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                val validValue =
                    if (action == Action.LOSS) {
                        movimentation.value.unaryMinus()
                    } else {
                        movimentation.value.unaryPlus()
                    }

                val formattedMovimentation =
                    movimentation.copy(
                        id = 0,
                        value = validValue,
                        spendAt = Calendar.getInstance().timeInMillis,
                    )
                val newMovimentation = financeUseCase.saveMovimentation(formattedMovimentation)

                handleCallBackSuccess(
                    movimentation.promptDescription(),
                    true,
                    extraKey = newMovimentation.toString(),
                ) {
                    generateSuggestions()
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
