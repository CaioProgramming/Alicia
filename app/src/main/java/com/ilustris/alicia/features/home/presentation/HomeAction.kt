package com.ilustris.alicia.features.home.presentation

import com.ilustris.alicia.features.messages.domain.model.Suggestion

sealed class HomeAction {

    object FetchUser : HomeAction()
    data class SaveUser(val name: String) : HomeAction()
    data class SaveLoss(val description: String, val value: String) : HomeAction()
    data class SaveProfit(val description: String, val value: String) : HomeAction()
    data class SaveGoal(val description: String, val value: String) : HomeAction()



}
