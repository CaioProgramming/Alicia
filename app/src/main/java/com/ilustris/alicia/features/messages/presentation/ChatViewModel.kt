package com.ilustris.alicia.features.messages.presentation


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.features.messages.data.datasource.MessagePresets
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.MessageInfo
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
class ChatViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val messagesUseCase: MessagesUseCase,
    private val financeUseCase: FinanceUseCase
) : ViewModel() {

    init {
        getUser()
    }

    val messages = messagesUseCase.getMessages()
    val profit = financeUseCase.getProfit()
    val loss = financeUseCase.getLoss()
    val amount = financeUseCase.getAmount()
    val goals = financeUseCase.getGoals()
    val showInput: MutableLiveData<Boolean> = MutableLiveData()
    val playNewMessage: MutableLiveData<Boolean> = MutableLiveData()

    private fun saveUser(name: String) {
        showInput.value = false
        viewModelScope.launch {
            userUseCase.saveUser(name)
            updateMessages(
                listOf(
                    Message("Oi Alicia pode me chamar de $name :)", Type.USER),
                    MessagePresets.getGreeting(name),
                )
            )
            delay(1000)
            updateMessages(MessagePresets.introductionMessages)
        }
    }

    fun launchAction(homeAction: ChatAction) {
        when (homeAction) {

            is ChatAction.SaveUser -> saveUser(homeAction.name)
            is ChatAction.SaveGoal -> saveGoal(
                homeAction.description,
                homeAction.value,
                homeAction.tag
            )
            is ChatAction.SaveLoss -> saveLoss(
                homeAction.description,
                homeAction.value,
                homeAction.tag,
                Type.LOSS
            )
            is ChatAction.SaveProfit -> saveProfit(
                homeAction.description,
                homeAction.value,
                homeAction.tag,
                Type.PROFIT
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
                    completedAt = Calendar.getInstance().timeInMillis
                )
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
                            extraActions = listOf(Action.GOAL.name).toString()
                        )
                    )
                } else {
                    updateMessages(
                        listOf(
                            Message("Opa é pra já :)"),
                            Message(
                                "Da uma olhada você já criou ${it.size} e concluiu ${it.filter { it.isComplete }.size}",
                                type = Type.GOAL
                            ),
                            Message(
                                "Continue assim, as metas nos motiva a guardar nosso dinheirinho",
                                extraActions = listOf(
                                    Action.PROFIT.name,
                                    Action.LOSS.name
                                ).toString()
                            )
                        )
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
                            extraActions = listOf(Action.PROFIT, Action.LOSS).toString()
                        )
                    )
                } else {
                    updateMessages(
                        listOf(
                            Message("É pra já! vou pegar essas informações para você, 1 minutinho por favor."),
                            Message("Da uma olhada no seu saldo", Type.AMOUNT),
                            Message(
                                "Vamos falar de gastos? Aqui estão todo seus gastos desde que começou a usar o app",
                                type = Type.LOSS_HISTORY
                            ),
                            Message(
                                "Seus rendimentos foram bem legais, da uma olhada.",
                                type = Type.PROFIT_HISTORY
                            ),
                            Message(
                                "É isso ai vamos continuar evoluindo :)",
                                extraActions = listOf(
                                    Action.PROFIT.name,
                                    Action.LOSS.name,
                                    Action.GOAL.name,
                                    Action.GOAL_HISTORY
                                ).toString()
                            )
                        )
                    )
                }
                coroutineContext.job.cancel()
            }

        }
    }

    private fun saveGoal(description: String, value: String, tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            financeUseCase.saveGoal(description, value, tag)
            updateMessages(
                Message(
                    "Meu próximo objetivo é juntar ${(value.toDouble() / 100).formatToCurrencyText()} para conseguir $description :D",
                    type = Type.USER
                )
            )
            updateMessages(
                listOf(
                    MessagePresets.getGoalMessage(tag, description),
                    Message(
                        "Cada meta é como uma medalha, você pode sempre acompanhar suas metas :P",
                        type = Type.GOAL
                    ),
                    Message(
                        "E vamos continuar registrando as entradas e saídas, vamos alcançar suas metas! :)",
                        extraActions = listOf(
                            Action.PROFIT.name,
                            Action.LOSS.name,
                            Action.HISTORY.name,
                        ).toString()
                    )
                )
            )
        }
    }

    private fun saveLoss(description: String, value: String, tag: Tag, type: Type) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedValue = (value.toDouble() / 100).formatToCurrencyText()
            financeUseCase.saveMovimentation(description, value, tag, type)
            updateMessages(
                Message(
                    "Tive que gastar $savedValue com $description...",
                    type = Type.USER
                )
            )
            updateMessages(MessagePresets.getLossMessage(savedValue, tag))
        }
    }

    private fun saveProfit(description: String, value: String, tag: Tag, type: Type) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedValue = (value.toDouble() / 100).formatToCurrencyText()
            financeUseCase.saveMovimentation(description, value, tag, type)
            updateMessages(Message("Consegui $savedValue com $description!", Type.USER))
            updateMessages(MessagePresets.getProfitMessage(savedValue, tag))
        }
    }


    private fun updateMessages(message: Message) {
        Log.i(javaClass.simpleName, "updateMessages: Updating messages adding -> $message")
        viewModelScope.launch(Dispatchers.IO) {
            val delayTime = if (message.type == Type.USER) 200L else 1000L
            delay(delayTime)
            messagesUseCase.saveMessage(message)
            playMessage()
        }
    }

    private fun playMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            playNewMessage.postValue(true)
            delay(1000)
            playNewMessage.postValue(false)
        }
    }

    private fun updateMessages(message: List<Message>, onSendMessages: (() -> Unit)? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            message.forEachIndexed { index, m ->
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
        val lastMessageDate = Calendar.getInstance().apply {
            timeInMillis = lastMessage.message.sentTime
        }
        return lastMessageDate[Calendar.DAY_OF_YEAR] < todayDate[Calendar.DAY_OF_YEAR]
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userUseCase.getUserById().collect { user ->
                showInput.postValue(user == null)
                val lastMessage = messagesUseCase.getLastMessage()
                if (user == null) {
                    if (shouldSendNewMessage(lastMessage)) {
                        updateMessages(MessagePresets.newUserMessages)
                    }
                } else {
                    if (shouldSendNewMessage(lastMessage)) {
                        updateMessages(MessagePresets.getGreeting(user.name))
                        updateMessages(MessagePresets.keepGoingMessage())
                    }
                }
                coroutineContext.job.cancel()
            }
        }
    }

}