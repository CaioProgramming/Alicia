package com.ilustris.alicia.features.messages.presentation

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag

sealed class HomeAction {

    object FetchUser : HomeAction()
    data class SaveUser(val name: String) : HomeAction()
    data class SaveLoss(val description: String, val value: String, val tag: Tag) : HomeAction()
    data class SaveProfit(val description: String, val value: String, val tag: Tag) : HomeAction()
    data class SaveGoal(val description: String, val value: String, val tag: Tag) : HomeAction()
    data class CompleteGoal(val goal: Goal) : HomeAction()
    object GetHistory : HomeAction()
    object GetGoals : HomeAction()

}
