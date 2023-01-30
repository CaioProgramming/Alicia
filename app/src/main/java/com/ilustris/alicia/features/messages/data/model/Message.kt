package com.ilustris.alicia.features.messages.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ilustris.alicia.features.finnance.data.model.Movimentation


@Entity
data class Message(
    val message: String,
    val type: Type = Type.NONE,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sentTime: Long = 0,
)


enum class Type {
    NONE, GAIN, LOSS, GOAL, NAME, USER, HEADER, AMOUNT, PROFIT_HISTORY, LOSS_HISTORY
}

