package com.ilustris.alicia.ai.model

import com.ilustris.alicia.features.messages.domain.model.Action

enum class Prompts(val prompt: String) {

    Greeting( "Give me a greeting message for the new user [username]"),
    Introduction("I need a array of messages introducing the app to the new user, please tell about these features: ${featuresExamples()} "),
    NewUser("Give me a nice greeting message asking user name, Do not use any placeholder on the message text."),
    Goal("Give me a nice message about the last goal sended by the user"),
    Loss("Give me a nice message about the last loss sended by the user"),
    Profit("Give me a nice message about the last profit sended by the user"),
    Suggestion("Give me a nice message suggesting user next action")
}

private fun featuresExamples() = Action
    .values()
    .filter { it != Action.NAME }
    .joinToString(","){ it.description }