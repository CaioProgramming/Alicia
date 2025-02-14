package com.ilustris.alicia.features.finnance.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.R
import com.ilustris.alicia.features.finnance.data.model.Goal
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

sealed class GoalSheetType(@StringRes val title: Int, @StringRes val description: Int) {
    data object Tutorial : GoalSheetType(R.string.avatar_introduction, R.string.goal_tutorial)
    data object NewGoal: GoalSheetType(R.string.new_goal, R.string.new_goal_description)
    data class UpdateGoal(val goal: Goal) : GoalSheetType(R.string.edit_goal, R.string.edit_goal_description)
}