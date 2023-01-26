package com.ilustris.alicia.features.home.presentation


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.features.finnance.data.model.Movimentation
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
    val movimentation = finnanceUseCase.getMovimentations()

    val suggestions: Flow<ArrayList<Suggestion>> = flowOf(ArrayList())

    init {
        getUser()
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
        viewModelScope.launch(Dispatchers.IO) {
            movimentation.collect { movimentations ->
                if (movimentations.isNotEmpty()) {
                    val amount = movimentations.sumOf { movements -> movements.value }
                    updateMessages(Message("Começando pelo saldo, você tem ${amount.formatToCurrencyText()}"))
                    val spendsList = movimentations.filter { it.value < 0 }
                    if (spendsList.isNotEmpty()) {
                        updateMessages(Message("Vamos falar de gastos? Aqui estão todo seus gastos desde que começou a usar o app: "))
                        updateMessages(Message(buildStatementList(spendsList)))
                    }

                    val profitList = movimentations.filter { it.value > 0 }
                    if (profitList.isNotEmpty()) {
                        updateMessages(Message("Seus rendimentos foram bem legais, da uma olhada"))
                        updateMessages(Message(buildStatementList(profitList)))
                    }
                    if (amount > 0 && spendsList.isNotEmpty()) {
                        updateMessages(Message("Mesmo com gastos você ainda está positivo, continue assim para alcançar suas metas! :)"))
                    }
                } else {
                    updateMessages(
                        Message(
                            "Pare que você não salvou nenhuma movimentação ainda, então não tem nada que eu possa te mostrar.",
                            Type.HISTORY
                        )
                    )
                }
                this.coroutineContext.job.cancel()
            }
        }
    }

    private fun buildStatementList(movimentations: List<Movimentation>): String {
        return movimentations.map {
            "${it.description}:  ${(it.value / 100).formatToCurrencyText()}"
        }.toString()
            .replace(",", "")  //remove the commas
            .replace("[", "")  //remove the right bracket
            .replace("]", "")  //remove the left bracket
            .trim()
    }

    private fun saveGoal(description: String, value: String) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    private fun saveLoss(description: String, value: String, type: Type) {
        viewModelScope.launch(Dispatchers.IO) {
            finnanceUseCase.saveMovimentation(description, value, type)
            updateMessages(Message("Tive que gastar com $description...", type = Type.USER))
            movimentation.collect {
                val newAmount = it.sumOf { movements -> movements.value }
                updateMessages(
                    Message(
                        MessagePresets.getLossMessage(
                            NumberFormat.getCurrencyInstance().format(newAmount)
                        )
                    )
                )
                this.coroutineContext.job.cancel()
            }
        }
    }

    private fun saveProfit(description: String, value: String, type: Type) {
        viewModelScope.launch(Dispatchers.IO) {
            finnanceUseCase.saveMovimentation(description, value, type)
            updateMessages(Message("Ganhei uma grana com $description!", Type.USER))
            movimentation.collect {
                val newAmount = it.sumOf { movements -> movements.value }
                updateMessages(
                    Message(
                        MessagePresets.getProfitMessage(
                            NumberFormat.getCurrencyInstance().format(newAmount)
                        )
                    )
                )
                this.coroutineContext.job.cancel()
            }
        }
    }

    private fun updateMessages(message: Message) {
        Log.i(javaClass.simpleName, "updateMessages: Updating messages adding -> $message")
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
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

}