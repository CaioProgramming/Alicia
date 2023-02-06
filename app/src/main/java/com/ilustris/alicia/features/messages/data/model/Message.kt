package com.ilustris.alicia.features.messages.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Message(
    val message: String,
    val type: Type = Type.NONE,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sentTime: Long = 0,
    val extraActions: String = ""
)

enum class Type {
    NONE, PROFIT, LOSS, GOAL, NAME, USER, HEADER, AMOUNT, PROFIT_HISTORY, LOSS_HISTORY
}

