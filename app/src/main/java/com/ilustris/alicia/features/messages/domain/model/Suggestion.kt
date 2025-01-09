package com.ilustris.alicia.features.messages.domain.model

import com.ilustris.alicia.ai.model.ai.AIResponse
import com.ilustris.alicia.ai.usecase.replaceClassIdentifier
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.utils.emptyString
import com.ilustris.alicia.utils.toJsonSchema

data class Suggestion(
    val action: Action,
    val description: String = emptyString(),
)

enum class Action {
    NAME,
    PROFIT,
    LOSS,
    GOAL,
    NONE,
}

fun Action.bodyClass(): Class<*> =
    when (this) {
        Action.NAME -> NameBody::class.java
        Action.PROFIT, Action.LOSS -> Movimentation::class.java
        Action.GOAL -> Goal::class.java
        Action.NONE -> AIResponse::class.java
    }

fun Action.actionBody(): String =
    when (this) {
        Action.NAME -> toJsonSchema<NameBody>()
        Action.PROFIT, Action.LOSS ->
            toJsonSchema<Movimentation>().replaceClassIdentifier(
                Tag::class.java.simpleName,
                "[${Tag.entries.joinToString(",") { it.name }}]",
            )
        Action.GOAL ->
            toJsonSchema<Goal>().replaceClassIdentifier(
                Tag::class.java.simpleName,
                "[${Tag.entries.joinToString(",") { it.name }}]",
            )
        Action.NONE -> toJsonSchema<AIResponse>()
    }

fun List<Action>.actionBodies(): String =
    this.joinToString(";\n") {
        it.actionBody()
    }

data class NameBody(
    val name: String,
)
