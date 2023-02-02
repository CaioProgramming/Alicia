package com.ilustris.alicia.features.home.presentation

import com.ilustris.alicia.features.finnance.data.model.Tag

sealed class HomeAction {

    object FetchUser : HomeAction()
    data class SaveUser(val name: String) : HomeAction()
    data class SaveLoss(val description: String, val value: String, val tag: Tag) : HomeAction()
    data class SaveProfit(val description: String, val value: String, val tag: Tag) : HomeAction()
    data class SaveGoal(val description: String, val value: String, val tag: Tag) : HomeAction()
    object GetHistory : HomeAction()


}
