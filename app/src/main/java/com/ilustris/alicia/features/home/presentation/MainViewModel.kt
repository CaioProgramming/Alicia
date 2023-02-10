package com.ilustris.alicia.features.home.presentation

import androidx.lifecycle.ViewModel
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(financeUseCase: FinanceUseCase, userUseCase: UserUseCase) :
    ViewModel() {

    val goals = financeUseCase.getGoals()
    val movimentations = financeUseCase.getMovimentationsByDay()
    val user = userUseCase.getUserById()


}