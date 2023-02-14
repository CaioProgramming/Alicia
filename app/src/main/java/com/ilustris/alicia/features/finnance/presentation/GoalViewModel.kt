package com.ilustris.alicia.features.finnance.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.utils.GOAL_KEY
import com.ilustris.alicia.utils.PreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    financeUseCase: FinanceUseCase,
    private val preferencesService: PreferencesService
) : ViewModel() {

    val goals = financeUseCase.getGoalsInfo()
    fun goalIntro() = preferencesService.getBooleanKey(GOAL_KEY)
    fun updateGoalKey() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesService.updateBooleanKey(GOAL_KEY, true)
        }
    }

}