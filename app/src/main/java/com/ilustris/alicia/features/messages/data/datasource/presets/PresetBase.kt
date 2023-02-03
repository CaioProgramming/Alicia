package com.ilustris.alicia.features.messages.data.datasource.presets


abstract class PresetBase {
    abstract val profitMessages: List<String>
    abstract val lossMessages: List<String>

    fun getProfitMessage(value: String) = profitMessages.random().replace(VALUE_PLACEHOLDER, value)
    fun getLossMessage(value: String) = lossMessages.random().replace(VALUE_PLACEHOLDER, value)
}

const val VALUE_PLACEHOLDER = "{value}"