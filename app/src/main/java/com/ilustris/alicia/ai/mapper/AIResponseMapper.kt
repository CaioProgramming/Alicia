package com.ilustris.alicia.ai.mapper

import android.util.Log
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.asTextOrNull
import com.ilustris.alicia.features.messages.data.model.Message
import com.google.gson.Gson
import java.util.Calendar


fun GenerateContentResponse.mapToMessage() : Message? {
    try {
        this.candidates.first().content.parts.first().asTextOrNull()?.let {
            Log.i(javaClass.simpleName, "mapToMessage: generated Response $it")
            var mappedResponse = it
                .substring(it.indexOf("{"), it.indexOf("}") + 1)
            if(mappedResponse.contains("[") && mappedResponse.contains("]")) {
                mappedResponse = removeBrackets(mappedResponse)
            }
            val responseData = Gson().fromJson(mappedResponse, Message::class.java)
            return responseData.copy(sentTime = Calendar.getInstance().time.time)
        } ?: return null
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

private fun removeBrackets(response: String): String {
    return response.replace("[", "\"").replace("]", "\"")
}