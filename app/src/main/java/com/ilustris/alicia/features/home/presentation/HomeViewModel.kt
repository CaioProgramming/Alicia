package com.ilustris.alicia.features.home.presentation


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.features.finnance.domain.usecase.FinnanceUseCase
import com.ilustris.alicia.features.messages.data.datasource.MessagePresets
import com.ilustris.alicia.features.messages.data.datasource.SuggestionsPresets
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.features.messages.domain.usecase.MessagesUseCase
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
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
    val movimentation = finnanceUseCase.getMovimentations()

    val messagesGroup: Flow<ArrayList<Message>> = flowOf(ArrayList())

    val suggestions: Flow<ArrayList<Suggestion>> = flowOf(ArrayList())

    init {
        getUser()
        mapMessages()
    }


    private fun saveUser(name: String) {
        viewModelScope.launch {
            userUseCase.saveUser(name)
            updateMessages(Message("Oi Alicia pode me chamar de $name :)", Type.USER))
            delay(5000)
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
            is HomeAction.SaveProfit -> saveProfit(homeAction.description, homeAction.value, Type.GAIN)
        }
    }

    private fun saveGoal(description: String, value: String) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    private fun saveLoss(description: String, value: String, type: Type) {
        viewModelScope.launch(Dispatchers.IO) {
            finnanceUseCase.saveMovimentation(description, value, type)
            updateMessages(Message("Eeeeita teve que gastar com $description..."))
            movimentation.collect {
                val newAmount = it.sumOf { movements -> movements.value }
                updateMessages(Message(MessagePresets.getLossMessage(NumberFormat.getCurrencyInstance().format(newAmount))))
                this.coroutineContext.job.cancel()
            }
        }
    }

    private fun saveProfit(description: String, value: String, type: Type) {
        viewModelScope.launch(Dispatchers.IO) {
            finnanceUseCase.saveMovimentation(description, value, type)
            updateMessages(Message("Hummm ganhou uma graninha com $description!"))
            movimentation.collect {
                val newAmount = it.sumOf { movements -> movements.value }
                updateMessages(Message(MessagePresets.getProfitMessage(NumberFormat.getCurrencyInstance().format(newAmount))))
                this.coroutineContext.job.cancel()
            }
        }
    }

    private fun updateMessages(message: Message) {
        Log.i(javaClass.simpleName, "updateMessages: Updating messages adding -> $message")
        viewModelScope.launch(Dispatchers.IO) {
            messagesUseCase.saveMessage(message)
        }
    }

    private fun updateMessages(message: List<Message>) {
        Log.i(javaClass.simpleName, "updateMessages: Updating messages adding -> $message")
        viewModelScope.launch(Dispatchers.IO) {
            message.forEach {
                delay(5000)
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
                    timeInMillis = lastMessage.sentTime
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

    private fun updatedGroupMessages(messages: List<Message>) {
        viewModelScope.launch(Dispatchers.IO) {
            messagesGroup.collect {
                it.clear()
                it.addAll(messages)
            }
        }
    }

    private fun mapMessages() = viewModelScope.launch {
        messages.collect {
            val messagesGrouped = ArrayList<Message>()
            val groupedByDay = it.groupBy {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = it.sentTime
                }
                calendar[Calendar.DAY_OF_YEAR]
            }
            groupedByDay.forEach { (day, messages) ->
                val firstMessage = messages.first()
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = firstMessage.sentTime
                }
                val dayMessage = firstMessage.copy(
                    message = calendar.time.format(DateFormats.EE_D_MMM_YYY),
                    type = Type.HEADER
                )
                messagesGrouped.add(dayMessage)
                messages.forEach { message ->
                    messagesGrouped.add(message)
                }
            }
            updatedGroupMessages(messagesGrouped)
        }
    }

}