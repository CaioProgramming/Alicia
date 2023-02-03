package com.ilustris.alicia.features.messages.data.datasource.presets

class ShoppingPresets : PresetBase() {
    override val lossMessages = listOf(
        "Todo mundo precisa de um mimo as vezes, o seu te custou $VALUE_PLACEHOLDER hahaha.",
        "Essa compra foi uma loucura hein teve que gastar $VALUE_PLACEHOLDER.",
        "Viva como se não houvesse amanhã, porque amanhã você já não vai ter $VALUE_PLACEHOLDER.",
    )

    override val profitMessages = listOf(
        "Ora ora hein ta parecendo um empresário $VALUE_PLACEHOLDER.",
        "Alguns dias somos o cliente outro o vendedor né, faturou $VALUE_PLACEHOLDER.",
        "Assim você vai ter que abrir sua própria loja, ganhou $VALUE_PLACEHOLDER."
    )
}