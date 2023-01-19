package com.ilustris.alicia.features.home.presentation


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.Message
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {


    val messages = MutableLiveData<ArrayList<Message>>()

    init {
        messages.value = ArrayList()
    }


    private fun saveUser(name: String) {
        viewModelScope.launch {
            userUseCase.saveUser(name)
        }
    }

    fun launchAction(homeAction: HomeAction) {
        when (homeAction) {
            HomeAction.FetchUser -> getUser()
            is HomeAction.SaveUser -> saveUser(homeAction.name)
        }
    }

    private fun updateMessages(message: Message) {
        Log.i(javaClass.simpleName, "updateMessages: Updating messages adding -> $message")
        val messagesArray  = messages.value
        if (messagesArray == null) {
            messages.postValue(ArrayList())
        }
        viewModelScope.launch(Dispatchers.IO) {
            var messagesArray = messages.value
            if (messagesArray == null) messagesArray = ArrayList()
            messagesArray.add(message)
            messages.postValue(messagesArray)
            Log.i(javaClass.simpleName, "updateMessages: new messages -> ${messages.value} ")

        }
    }

    private fun getUser() {
        Log.i(javaClass.simpleName, "getUser: searching user")
        viewModelScope.launch(Dispatchers.IO) {
            userUseCase.getUserById()
                .catch { exception ->
                    exception.printStackTrace()
                    Log.e(
                        javaClass.simpleName,
                        "getUser: Ocorreu um erro inesperado ${exception.message}"
                    )
                }
                .collect { user ->
                    Log.i(javaClass.simpleName, "getUser query result -> $user")

                    if (user == null) {
                        val message = Message(
                            "Oiii, parece que você é novo por aqui...\n Me fala ai... Como posso te chamar",
                            Action.NAME
                        )
                        Log.i(javaClass.simpleName, "send null user message -> $message")

                        updateMessages(message)
                    } else {
                        val message = Message(
                            "Oiii ${user.name}, bom te ver por aqui..\nNovidades para salvarmos aqui?",
                            Action.NONE
                        )
                        Log.i(javaClass.simpleName, "send founded user message -> $message")
                        updateMessages(message)
                    }
                }

        }
    }


}