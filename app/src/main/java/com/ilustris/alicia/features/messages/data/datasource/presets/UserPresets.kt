package com.ilustris.alicia.features.messages.data.datasource.presets


object UserPresets {

    private val greetingMessages = listOf(
        "Oie $VALUE_PLACEHOLDER",
        "Oie $VALUE_PLACEHOLDER",
        "Ei $VALUE_PLACEHOLDER, tudo bem?",
        "Oiii $VALUE_PLACEHOLDER.",
        "Salve salve $VALUE_PLACEHOLDER",
        "Olá olá $VALUE_PLACEHOLDER",
        "Você de novo $VALUE_PLACEHOLDER?"
    )

    fun getGreetingMessage(userName: String) =
        greetingMessages.random().replace(VALUE_PLACEHOLDER, userName)
}