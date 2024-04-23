package com.ilustris.alicia.features.messages.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.ai.model.Prompts
import com.ilustris.alicia.ai.usecase.AIUseCase
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.MessageInfo
import com.ilustris.alicia.features.messages.domain.model.replacePlaceHolder
import com.ilustris.alicia.features.messages.domain.usecase.MessagesUseCase
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import com.ilustris.alicia.utils.formatToCurrencyText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
@Inject
constructor(
    private val userUseCase: UserUseCase,
    private val messagesUseCase: MessagesUseCase,
    private val financeUseCase: FinanceUseCase,
    private val aiUseCase: AIUseCase,
) : ViewModel() {
    init {
        getUser()
    }

    val messages = messagesUseCase.getMessages()
    val profit = financeUseCase.getProfit()
    val loss = financeUseCase.getLoss()
    val amount = financeUseCase.getAmount()
    val goals = financeUseCase.getGoals()
    val playNewMessage: MutableLiveData<Boolean> = MutableLiveData()
    val user = userUseCase.getUserById()

    private fun sendIntroductionMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            val introductionMessage =
                aiUseCase.requestMessage(
                    Prompts.Introduction.prompt,
                    listOf(
                        PromptConfig.ArrayConfig(Message.getBody()),
                    ),
                )

            updateMessages(introductionMessage) {
                generateSuggestionMessage()
            }
        }
    }

    private suspend fun sendGreeting(userName: String) {
        val prompt = Prompts.Greeting.prompt.replace("[username]", userName)
        aiUseCase.requestMessage(
            prompt,
            listOf(
                PromptConfig.ArrayConfig(
                    Message.getBody(),
                ),
            ),
        )?.run {
            updateMessages(this, ::sendIntroductionMessages)

        }
    }

    private fun saveUser(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userUseCase.saveUser(name)

            updateMessages(Message("Oi Alicia pode me chamar de $name :)", Type.USER)) {
                sendGreeting(name)
            }

        }
    }

    fun launchAction(homeAction: ChatAction) {
        when (homeAction) {
            is ChatAction.SaveUser -> saveUser(homeAction.name)
            is ChatAction.SaveGoal ->
                saveGoal(
                    homeAction.description,
                    homeAction.value,
                    homeAction.tag,
                )
            is ChatAction.SaveLoss ->
                saveLoss(
                    homeAction.description,
                    homeAction.value,
                    homeAction.tag,
                    Type.LOSS,
                )
            is ChatAction.SaveProfit ->
                saveProfit(
                    homeAction.description,
                    homeAction.value,
                    homeAction.tag,
                    Type.PROFIT,
                )
            ChatAction.GetHistory -> getHistory()
            ChatAction.GetGoals -> getGoals()
            is ChatAction.CompleteGoal -> completeGoal(homeAction.goal)
            ChatAction.StopNewMessageAudio -> playNewMessage.postValue(false)
            ChatAction.FetchUser -> getUser()
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

    private fun getGoals() {
        updateMessages(Message("Quero ver minhas metas.", Type.USER))
        viewModelScope.launch(Dispatchers.IO) {
            goals.collect {
                if (it.isEmpty()) {
                    updateMessages(
                        Message(
                            "Parece que você não criou nenhuma meta ainda. Cria uma agora para começarmos a acompanhar 😗",
                            type = Type.GOAL,
                            extraActions = listOf(Action.GOAL.name).toString(),
                        ),
                    )
                } else {
                    updateMessages(
                        listOf(
                            Message("Opa é pra já :)"),
                            Message(
                                "Da uma olhada você já criou ${it.size} e concluiu ${it.filter { it.isComplete }.size}",
                                type = Type.GOAL,
                            ),
                            Message(
                                "Continue assim, as metas nos motiva a guardar nosso dinheirinho",
                                extraActions =
                                listOf(
                                    Action.PROFIT.name,
                                    Action.LOSS.name,
                                ).toString(),
                            ),
                        ),
                    )
                }
                coroutineContext.job.cancel()
            }
        }
    }

    private fun getHistory() {
        updateMessages(Message("Quero ver meu histórico de transações.", Type.USER))
        viewModelScope.launch(Dispatchers.IO) {
            amount.collect { currentAmount ->
                if (currentAmount == 0.0) {
                    updateMessages(
                        Message(
                            "Você ainda não salvou nenhuma movimentação, comece a salvar alguns rendimentos ou gastos para conseguirmos ver aqui 😃.",
                            extraActions = listOf(Action.PROFIT, Action.LOSS).toString(),
                        ),
                    )
                } else {
                    updateMessages(
                        listOf(
                            Message("É pra já! vou pegar essas informações para você, 1 minutinho por favor."),
                            Message("Da uma olhada no seu saldo", Type.AMOUNT),
                            Message(
                                "Vamos falar de gastos? Aqui estão todo seus gastos desde que começou a usar o app",
                                type = Type.LOSS_HISTORY,
                            ),
                            Message(
                                "Seus rendimentos foram bem legais, da uma olhada.",
                                type = Type.PROFIT_HISTORY,
                            ),
                            Message(
                                "É isso ai vamos continuar evoluindo :)",
                                extraActions =
                                listOf(
                                    Action.PROFIT.name,
                                    Action.LOSS.name,
                                    Action.GOAL.name,
                                    Action.GOAL_HISTORY,
                                ).toString(),
                            ),
                        ),
                    )
                }
                coroutineContext.job.cancel()
            }
        }
    }

    private fun saveGoal(
        description: String,
        value: String,
        tag: Tag,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val goalDescription = "$description: $value, $tag"
            val action = Action.GOAL
            val message =
                aiUseCase
                    .generateNewMessageForAction(
                        Action.GOAL,
                        action.replacePlaceHolder(goalDescription),
                    )

            financeUseCase.saveGoal(description, value, tag)
            updateMessages(
                message?.copy(type = Type.USER),
            )

            val responseMessage =
                aiUseCase.requestMessage(
                    Prompts.Goal.prompt,
                    listOf(
                        PromptConfig.BodyConfig(Message.getBody()),
                    ),
                )?.copy(type = Type.GOAL)

            updateMessages(
                listOf(
                    responseMessage,
                ),
            )

            generateSuggestionMessage()
        }
    }

    private fun generateSuggestionMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            val message = aiUseCase.requestSuggestionsMessage()
            updateMessages(message)
        }
    }

    private fun saveLoss(
        description: String,
        value: String,
        tag: Tag,
        type: Type,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val movimentationTask = financeUseCase.saveMovimentation(description, value, tag, type)
            val action = Action.LOSS
            val message =
                aiUseCase.generateNewMessageForAction(
                    action,
                    action.replacePlaceHolder(movimentationTask.toString()),
                )
            updateMessages(
                message,
            )
            aiUseCase.requestMessage(Prompts.Loss.prompt, listOf())
        }
    }

    private fun saveProfit(
        description: String,
        value: String,
        tag: Tag,
        type: Type,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedValue = (value.toDouble() / 100).formatToCurrencyText()
            financeUseCase.saveMovimentation(description, value, tag, type)
            val action = Action.PROFIT
            val message =
                aiUseCase.generateNewMessageForAction(
                    action,
                    action.replacePlaceHolder(savedValue),
                )
            updateMessages(message)

            val responseMessage =
                aiUseCase.requestMessage(
                    Prompts.Profit.prompt,
                    listOf(
                        PromptConfig.BodyConfig(Message.getBody()),
                    ),
                )

            updateMessages(responseMessage)
        }
    }

    private fun updateMessages(
        message: Message?,
        onSendMessages: (suspend () -> Unit)? = null,
    ) {
        Log.i(javaClass.simpleName, "updateMessages: Updating messages adding -> $message")
        viewModelScope.launch(Dispatchers.IO) {
            message?.let {
                messagesUseCase.saveMessage(it.copy(sentTime = Calendar.getInstance().timeInMillis))
                playMessage()
                onSendMessages?.invoke()
            }
        }
    }

    private fun playMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            playNewMessage.postValue(true)
            delay(1000)
            playNewMessage.postValue(false)
        }
    }

    private fun updateMessages(
        message: List<Message?>,
        onSendMessages: (() -> Unit)? = null,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            message.filterNotNull().forEachIndexed { index, m ->
                delay((100 * message.size).toLong())
                messagesUseCase.saveMessage(m)
                if (index == message.size - 1) {
                    playMessage()
                    onSendMessages?.invoke()
                }
            }
        }
    }

    private fun shouldSendNewMessage(lastMessage: MessageInfo?): Boolean {
        if (lastMessage == null) return true
        val todayDate = Calendar.getInstance()
        val lastMessageDate =
            Calendar.getInstance().apply {
                timeInMillis = lastMessage.message.sentTime
            }
        return lastMessageDate[Calendar.DAY_OF_YEAR] < todayDate[Calendar.DAY_OF_YEAR]
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userUseCase.getUserById().collect { user ->
                val lastMessage = messagesUseCase.getLastMessage()
                if (user == null) {
                    if (shouldSendNewMessage(lastMessage)) {
                        val generatedMessage = aiUseCase.requestNewUserMessage()
                        updateMessages(generatedMessage)
                    }
                } else {
                    if (shouldSendNewMessage(lastMessage)) {
                        val prompt = Prompts.Greeting.prompt.replace("[username]", user.name)
                        val configs = listOf(PromptConfig.BodyConfig(Message.getBody()))
                        val greetMessage = aiUseCase.requestMessage(prompt, configs)
                        updateMessages(greetMessage)
                    }
                }
                coroutineContext.job.cancel()
            }
        }
    }
}
