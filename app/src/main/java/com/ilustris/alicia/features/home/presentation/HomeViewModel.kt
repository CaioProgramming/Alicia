package com.ilustris.alicia.features.home.presentation


import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.features.messages.data.datasource.MessagePresets
import com.ilustris.alicia.features.messages.data.model.Action
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.features.messages.domain.usecase.MessagesUseCase
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val messagesUseCase: MessagesUseCase
) : ViewModel() {

    init {
        getUser()
    }


    val messages = messagesUseCase.getMessages()

    val suggestions: Flow<ArrayList<Suggestion>> = flowOf(ArrayList())

    private fun saveUser(name: String) {
        viewModelScope.launch {
            userUseCase.saveUser(name)
            updateMessages(Message("Oi Alicia pode me chamar de $name :)", Action.USER))
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
            is HomeAction.SaveLoss -> saveLoss(homeAction.description, homeAction.value)
            is HomeAction.SaveProfit -> saveProfit(homeAction.description, homeAction.value)
        }
    }

    private fun saveGoal(description: String, value: String) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    private fun saveLoss(description: String, value: String) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    private fun saveProfit(description: String, value: String) {
        viewModelScope.launch(Dispatchers.IO) {

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
                it.addAll(MessagePresets.newUserSuggestions)
            }
        }
    }

    private fun updateSuggestionsForDefaultActions() {
        viewModelScope.launch(Dispatchers.IO) {
            suggestions.collect {
                it.clear()
                it.addAll(MessagePresets.commonSuggestions())
            }
        }
    }


}