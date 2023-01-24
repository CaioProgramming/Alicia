package com.ilustris.alicia.features.messages.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Message(
    val message: String,
    val action: Action = Action.NONE,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sentTime: Long = 0,
)


enum class Action {
    NONE, GAIN, LOSS, GOAL, NAME, USER, HEADER
}

