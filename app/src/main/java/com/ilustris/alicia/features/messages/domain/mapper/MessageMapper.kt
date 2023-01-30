package com.ilustris.alicia.features.messages.domain.mapper

import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.MessageInfo

class MessageMapper {


    fun mapMessageToInfo(
        message: Message,
        extraItems: List<Movimentation> = emptyList(),

    ) = MessageInfo(message, extraItems)

}