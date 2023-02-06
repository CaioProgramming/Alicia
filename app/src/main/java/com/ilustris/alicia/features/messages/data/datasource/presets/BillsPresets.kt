package com.ilustris.alicia.features.messages.data.datasource.presets


class BillsPresets : PresetBase() {
    override val lossMessages = listOf(
        "Dias de luta e dias de luta né fazer o que menos $VALUE_PLACEHOLDER pra nós.",
        "Ainda bem que eu não tenho que trabalhar pra ver $VALUE_PLACEHOLDER indo embora em boleto.",
        "Você sabe que os boletos fazem parte da nossa vida então vamos aceitar que já era $VALUE_PLACEHOLDER.",
    )

    override val profitMessages = listOf(
        "Ora ora hein eu nunca imaginei que seriamos quem entrega os boleto e não quem paga, mas o importante é que ganhamos $VALUE_PLACEHOLDER.",
        "Olha se vc está trabalhando em um banco me fala logo ta? Mais $VALUE_PLACEHOLDER pra conta.",
        "Sensacional!! Vida de executivo é assim mais recebe que paga $VALUE_PLACEHOLDER."
    )

    override val goalMessages: List<String> = emptyList()

}