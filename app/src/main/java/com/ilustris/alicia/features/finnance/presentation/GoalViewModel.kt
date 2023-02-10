package com.ilustris.alicia.features.finnance.presentation

import androidx.lifecycle.ViewModel
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(financeUseCase: FinanceUseCase) : ViewModel() {

    val goals = financeUseCase.getGoalsInfo()


}