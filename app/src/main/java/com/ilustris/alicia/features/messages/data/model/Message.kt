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
)


enum class Type {
    NONE, GAIN, LOSS, GOAL, NAME, USER, HEADER, HISTORY
}

