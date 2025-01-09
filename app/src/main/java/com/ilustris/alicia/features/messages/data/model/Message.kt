package com.ilustris.alicia.features.messages.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val message: String,
    val sender: Sender = Sender.USER,
    val type: String? = null,
    val sentTime: Long = Calendar.getInstance().time.time,
    val extraDataKey: String? = null,
    @Ignore
    var extraData: Any? = null,
) {
    constructor(
        id: Int,
        message: String,
        sender: Sender,
        type: String?,
        sentTime: Long,
        extraDataKey: String?,
    ) : this(id, message, sender, type, sentTime, extraDataKey, null)

    fun findType() = Type.entries.find { it.name == type }
}

enum class Sender {
    USER,
    BOT,
}

enum class Type {
    MOVIMENTATION,
    GOAL,
    AMOUNT,
    HISTORY,
}
