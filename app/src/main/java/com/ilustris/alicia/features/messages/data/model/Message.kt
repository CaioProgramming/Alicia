package com.ilustris.alicia.features.messages.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Calendar
import java.util.Date


@Entity
data class Message(
    val message: String,
    val type: Type = Type.NONE,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sentTime: Long = Calendar.getInstance().time.time,
    val extraActions: String = "",
    val sender: Sender = Sender.USER
) {

    companion object {
        fun getBody() = "{ message: message, type: type, extraActions: string, sender: sender  } IMPORTANT: Always use that structure don't replace any field and dont include any other field. dont use brackets on extra actions the list is a string separated by commas."
    }

}

enum class Sender {
    USER, BOT
}
enum class Type {
    NONE, PROFIT, LOSS, GOAL, NAME, USER, HEADER, AMOUNT, PROFIT_HISTORY, LOSS_HISTORY
}

