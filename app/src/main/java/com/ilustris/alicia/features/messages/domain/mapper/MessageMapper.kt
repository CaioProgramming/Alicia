package com.ilustris.alicia.features.messages.domain.mapper

import android.util.Log
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.MessageInfo
import com.ilustris.alicia.features.messages.domain.model.Suggestion

class MessageMapper {


    fun mapMessageToInfo(
        message: Message,

        ): MessageInfo {

        val suggestions = ArrayList<Suggestion>()
        val actions = message.extraActions.removeSurrounding("[", "]")
        Log.i(
            javaClass.simpleName,
            "mapMessageToInfo: stored Actions -> ${Action.values().map { it.name }}"
        )
        if (actions.isNotEmpty() && actions.contains(",")) {
            val actionList = actions.split(",")
            Log.i(javaClass.simpleName, "actions -> $actionList")
            actionList.forEachIndexed { index, action ->
                var actionValue = action.trim()
                actionValue = if (index == 0) {
                    actionValue.replace("[", "")
                } else if (index == actionList.size - 1) {
                    actionValue.replace("]", "")
                } else {
                    actionValue
                }
                Log.i(javaClass.simpleName, "searching action $actionValue")
                val actionEnum = Action.values().firstOrNull { it.name.contains(actionValue, true) }
                actionEnum?.let { enum ->
                    suggestions.add(Suggestion(enum))
                }
            }
        } else {
            Log.d(javaClass.simpleName, "Searching single action -> $actions")
            val actionEnum = Action.values().firstOrNull { it.name.contains(actions.trim(), true) }
            actionEnum?.let {
                Log.e(javaClass.simpleName, "Single action founded: ${it.name}")
                suggestions.add(Suggestion(it))
            }
        }
        Log.w(javaClass.simpleName, "Extra actions -> ${suggestions.map { it.action.name }} ")
        return MessageInfo(message, attachedSuggestions = suggestions)
    }

}