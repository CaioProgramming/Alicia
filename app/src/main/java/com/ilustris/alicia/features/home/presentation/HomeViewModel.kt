package com.ilustris.alicia.features.home.presentation


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.features.finnance.domain.usecase.FinnanceUseCase
import com.ilustris.alicia.features.messages.data.datasource.MessagePresets
import com.ilustris.alicia.features.messages.data.datasource.SuggestionsPresets
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.model.MessageInfo
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.features.messages.domain.usecase.MessagesUseCase
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import com.ilustris.alicia.utils.formatToCurrencyText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Calendar
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
    val movimentations = finnanceUseCase.getAllMovimentations()

    val suggestions: Flow<ArrayList<Suggestion>> = flowOf(ArrayList())

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
        when (homeAction) {
            HomeAction.FetchUser -> getUser()
            is HomeAction.SaveUser -> saveUser(homeAction.name)
            is HomeAction.SaveGoal -> saveGoal(homeAction.description, homeAction.value)
            is HomeAction.SaveLoss -> saveLoss(homeAction.description, homeAction.value, Type.LOSS)
            is HomeAction.SaveProfit -> saveProfit(
                homeAction.description,
                homeAction.value,
                Type.GAIN
            )
            HomeAction.GetHistory -> getHistory()
        }
    }

    private fun getHistory() {
        updateMessages(Message("Quero ver meu histórico de transações", Type.USER))
        updateMessages(Message("É pra já! vou pegar essas informações para você, 1 minutinho por favor."))
        updateMessages(Message("Da uma olhada no seu saldo", Type.AMOUNT))
        updateMessages(Message("Vamos falar de gastos? Aqui estão todo seus gastos desde que começou a usar o app",  type = Type.LOSS_HISTORY))
        updateMessages(Message("Seus rendimentos foram bem legais, da uma olhada.", type = Type.PROFIT_HISTORY))
    }

    private fun saveGoal(description: String, value: String) {
        viewModelScope.launch(Dispatchers.IO) {
        }
    }

    private fun saveLoss(description: String, value: String, type: Type) {
        viewModelScope.launch(Dispatchers.IO) {
            finnanceUseCase.saveMovimentation(description, value, type)
            updateMessages(Message("Tive que gastar ${(value.toDouble() / 100).formatToCurrencyText()} com $description...", type = Type.USER))

        }
    }

    private fun saveProfit(description: String, value: String, type: Type) {
        viewModelScope.launch(Dispatchers.IO) {
            finnanceUseCase.saveMovimentation(description, value, type)
            updateMessages(Message("Consegui ${(value.toDouble() / 100).formatToCurrencyText()} com $description!", Type.USER))
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

    private fun updateMessages(message: List<Message>) {
        Log.i(javaClass.simpleName, "updateMessages: Updating messages adding -> $message")
        viewModelScope.launch(Dispatchers.IO) {
            message.forEach {
                delay(2000)
                messagesUseCase.saveMessage(it)
            }
        }
    }


    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userUseCase.getUserById()
            if (user == null) {
                if (messages.first().isEmpty()) {
                    updateMessages(MessagePresets.newUserMessages)
                }
                updateSuggestionsForNewUser()
            } else {
                val lastMessage = messages.first().last()
                val today = Calendar.getInstance()
                val lastMessageDate = Calendar.getInstance().apply {
                    timeInMillis = lastMessage.message.sentTime
                }
                if (lastMessageDate.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR)) {
                    updateMessages(MessagePresets.getGreeting(user.name))
                }
                updateSuggestionsForDefaultActions()
            }
        }
    }

    private fun updateSuggestionsForNewUser() {
        viewModelScope.launch(Dispatchers.IO) {
            suggestions.collect {
                it.clear()
                it.addAll(SuggestionsPresets.newUserSuggestions)
            }
        }
    }

    private fun updateSuggestionsForDefaultActions() {
        viewModelScope.launch(Dispatchers.IO) {
            suggestions.collect {
                it.clear()
                it.addAll(SuggestionsPresets.commonSuggestions())
            }
        }
    }

}