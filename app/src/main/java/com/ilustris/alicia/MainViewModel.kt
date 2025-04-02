package com.ilustris.alicia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.core.navigation.Routes
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.features.messages.domain.usecase.ChatUseCase
import com.ilustris.alicia.features.user.data.model.User
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val financeUseCase: FinanceUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    val event: MutableStateFlow<MainEvent?> = MutableStateFlow(null)
    val user = userUseCase.getUserById()

    fun checkMovimentations() {
        viewModelScope.launch(Dispatchers.IO) {
            val movimentations = financeUseCase.getAllMovimentationsSync()
            val currentUser = userUseCase.getUserByIdAsync()
            if (movimentations.isEmpty() || currentUser == null) {
                event.emit(MainEvent.Navigate(Routes.CHAT.name))
            }
        }
    }

}

sealed class MainEvent {
    data class Navigate(val route: String) : MainEvent()
}