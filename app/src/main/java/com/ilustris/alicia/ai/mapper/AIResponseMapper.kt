package com.ilustris.alicia.ai.mapper

import android.util.Log
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.gson.Gson
import com.ilustris.alicia.utils.removeDuplicatedQuotes
import com.ilustris.alicia.utils.removeLineBreaks
import com.ilustris.alicia.utils.removeSlashes

fun <T> GenerateContentResponse.mapTo(clazz: Class<*>): T? {
    try {
        val content =
            this.candidates
                .first()
                .content.parts
                .first()
                .asTextOrNull() ?: return null

        Log.i(javaClass.simpleName, "mapTo: generated Response\n$content")
        var mappedResponse =
            content
                .substring(content.indexOf("{"), content.lastIndexOf("}") + 1)
                .removeSlashes()
                .removeLineBreaks()
                .removeDuplicatedQuotes()
        Log.i(javaClass.simpleName, "mapTo: parsing $mappedResponse")
        return Gson().fromJson(mappedResponse, clazz) as? T
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

// help me write a function to format that string and removing unnecessary line breaks, spaces, and brackets
fun formatResponse(response: String): String =
    response
        .removeLineBreaks()
        .removeSlashes()

fun removeBrackets(response: String): String {
    if (response.contains("[") && response.contains("]")) {
        return response.replace("[", "\"").replace("]", "\"")
    }
    return response
}
