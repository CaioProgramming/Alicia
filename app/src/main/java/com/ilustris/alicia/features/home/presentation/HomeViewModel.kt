package com.ilustris.alicia.features.home.presentation


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.usecase.FinnanceUseCase
import com.ilustris.alicia.features.messages.data.datasource.MessagePresets
import com.ilustris.alicia.features.messages.data.datasource.SuggestionsPresets
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.MessageInfo
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.features.messages.domain.usecase.MessagesUseCase
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import com.ilustris.alicia.utils.formatToCurrencyText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val messagesUseCase: MessagesUseCase,
    private val finnanceUseCase: FinnanceUseCase
) : ViewModel() {


    val messages = messagesUseCase.getMessages()
    val profit = finnanceUseCase.getProfit()
    val loss = finnanceUseCase.getLoss()
    val amount = finnanceUseCase.getAmount()
    val aliciaTyping = MutableLiveData<Boolean>()

    val suggestions: MutableLiveData<ArrayList<Suggestion>> = MutableLiveData()

    init {
        getUser()
    }


    private fun saveUser(name: String) {
        viewModelScope.launch {
            userUseCase.saveUser(name)
            updateMessages(Message("Oi Alicia pode me chamar de $name :)", Type.USER))
            updateMessages(MessagePresets.getGreeting(name))
            updateMessages(MessagePresets.introductionMessages)
            updateSuggestionsForDefaultActions()

        }
    }


    fun launchAction(homeAction: HomeAction) {
        hideActions()
        when (homeAction) {
            HomeAction.FetchUser -> getUser()
            is HomeAction.SaveUser -> saveUser(homeAction.name)
            is HomeAction.SaveGoal -> saveGoal(
                homeAction.description,
                homeAction.value,
                homeAction.tag
            )
            is HomeAction.SaveLoss -> saveLoss(
                homeAction.description,
                homeAction.value,
                homeAction.tag,
                Type.LOSS
            )
            is HomeAction.SaveProfit -> saveProfit(
                homeAction.description,
                homeAction.value,
                homeAction.tag,
                Type.GAIN
            )
            HomeAction.GetHistory -> getHistory()
        }
    }

    private fun getHistory() {
        updateMessages(Message("Quero ver meu histórico de transações", Type.USER))
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
                )
            )
        ) {
            updateSuggestionsForDefaultActions()
        }

    }

    private fun saveGoal(description: String, value: String, tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
        }
    }

    private fun saveLoss(description: String, value: String, tag: Tag, type: Type) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedValue = (value.toDouble() / 100).formatToCurrencyText()
            finnanceUseCase.saveMovimentation(description, value, tag, type)
            updateMessages(
                Message(
                    "Tive que gastar $savedValue com $description...",
                    type = Type.USER
                )
            )
            updateMessages(Message(MessagePresets.getLossMessage(savedValue)))
            updateSuggestionsForDefaultActions()
        }
    }

    private fun saveProfit(description: String, value: String, tag: Tag, type: Type) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedValue = (value.toDouble() / 100).formatToCurrencyText()
            finnanceUseCase.saveMovimentation(description, value, tag, type)
            updateMessages(Message("Consegui $savedValue com $description!", Type.USER))
            updateMessages(Message(MessagePresets.getProfitMessage(savedValue)))
            updateSuggestionsForDefaultActions()
        }
    }


    private fun updateMessages(message: Message) {
        Log.i(javaClass.simpleName, "updateMessages: Updating messages adding -> $message")
        viewModelScope.launch(Dispatchers.IO) {
            val delayTime = if (message.type == Type.USER) 200L else 1500L
            delay(delayTime)
            messagesUseCase.saveMessage(message)
        }
    }

    private fun updateMessages(message: List<Message>, onSendMessages: (() -> Unit)? = null) {
        Log.i(javaClass.simpleName, "updateMessages: Updating messages adding -> $message")
        viewModelScope.launch(Dispatchers.IO) {
            message.forEachIndexed { index, m ->
                delay(2000)
                messagesUseCase.saveMessage(m)
                if (index == message.size - 1) {
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
            val user = userUseCase.getUserById()
            val lastMessage = messagesUseCase.getLastMessage()
            if (user == null) {
                if (shouldSendNewMessage(lastMessage)) updateMessages(MessagePresets.newUserMessages)
                updateSuggestionsForNewUser()
            } else {
                if (shouldSendNewMessage(lastMessage)) {
                    updateMessages(MessagePresets.getGreeting(user.name))
                }
                updateSuggestionsForDefaultActions()
            }
        }
    }

    private fun updateSuggestionsForNewUser() {
        suggestions.postValue(ArrayList(SuggestionsPresets.newUserSuggestions))
    }

    private fun updateSuggestionsForDefaultActions() {
        suggestions.postValue(ArrayList(SuggestionsPresets.commonSuggestions))

    }

    private fun hideActions() {
        viewModelScope.launch(Dispatchers.IO) {
            suggestions.postValue(ArrayList())
        }
    }

}