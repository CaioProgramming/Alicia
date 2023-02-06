package com.ilustris.alicia.features.messages.data.datasource.presets


abstract class PresetBase {
    abstract val profitMessages: List<String>
    abstract val lossMessages: List<String>
    abstract val goalMessages: List<String>

    fun getProfitMessage(value: String) = profitMessages.random().replace(VALUE_PLACEHOLDER, value)
    fun getLossMessage(value: String) = lossMessages.random().replace(VALUE_PLACEHOLDER, value)
    fun getGoalMessage(description: String) =
        goalMessages.random().replace(VALUE_PLACEHOLDER, description)

}

const val VALUE_PLACEHOLDER = "{value}"