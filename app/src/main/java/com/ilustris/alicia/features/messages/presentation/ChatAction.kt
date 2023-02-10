package com.ilustris.alicia.features.messages.presentation

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag

sealed class ChatAction {

    object FetchUser : ChatAction()
    data class SaveUser(val name: String) : ChatAction()
    data class SaveLoss(val description: String, val value: String, val tag: Tag) : ChatAction()
    data class SaveProfit(val description: String, val value: String, val tag: Tag) : ChatAction()
    data class SaveGoal(val description: String, val value: String, val tag: Tag) : ChatAction()
    data class CompleteGoal(val goal: Goal) : ChatAction()
    object GetHistory : ChatAction()
    object GetGoals : ChatAction()

    object StopNewMessageAudio : ChatAction()

}
