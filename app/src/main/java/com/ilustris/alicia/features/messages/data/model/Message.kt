package com.ilustris.alicia.features.messages.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ilustris.alicia.core.navigation.Routes
import java.util.Calendar

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val sender: Sender = Sender.USER,
    val type: String? = null,
    val sentTime: Long = Calendar.getInstance().time.time,
    val extraDataKey: String? = null,
    @Ignore
    var extraData: Any? = null,
) {
    constructor(
        id: Int,
        text: String,
        sender: Sender,
        type: String?,
        sentTime: Long,
        extraDataKey: String?,
    ) : this(id, text, sender, type, sentTime, extraDataKey, null)

    fun findType() = Type.entries.find { it.name == type }
}

enum class Sender {
    USER,
    BOT,
}

enum class Type(
    val route: String,
) {
    MOVIMENTATION(Routes.STATEMENT.name),
    GOAL(Routes.GOAL.name),
    BALANCE(Routes.STATEMENT.name),
    HISTORY(Routes.STATEMENT.name),
}
