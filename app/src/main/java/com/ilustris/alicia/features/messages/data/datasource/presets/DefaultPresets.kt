package com.ilustris.alicia.features.messages.data.datasource.presets


class DefaultPresets : PresetBase() {


    override val profitMessages = listOf(
        "Ai simm hein!! Com isso você tem guardado $VALUE_PLACEHOLDER.",
        "Boooa! Mais $VALUE_PLACEHOLDER na conta!",
        "Bom trabalho de pouquinho em poquinho vamos chegando nas nossas metas, com mais esse dinheirinho vc já tem guardou mais $VALUE_PLACEHOLDER.",
        "Nossa nossa hein! Me paga um pastel depois, ganhou $VALUE_PLACEHOLDER.",
        "Shoow agora são mais $VALUE_PLACEHOLDER!"
    )

    override val lossMessages = listOf(
        "Puuts! Fazer oque né as vezes temos que gastar uns trocados mesmo, agora na conta tem $VALUE_PLACEHOLDER.",
        "Ok anotado seu gasto de $VALUE_PLACEHOLDER.",
        "É por isso que dizem que tem os dias de luta e os dias de glória né, hoje a luta foi no seu bolso hahaha acabou gastando $VALUE_PLACEHOLDER.",
        "Quem disse que a vida do brasileiro é moleza? Não é moleza e ainda pode custar $VALUE_PLACEHOLDER.",
        "A vida do trabalhador não é fácil mesmo! Ainda bem que eu sou só um aplicativo, vc acabou de perder $VALUE_PLACEHOLDER na conta."
    )

    override val goalMessages: List<String> = listOf(
        "Woow! Que legal sua meta $VALUE_PLACEHOLDER! vamos acompanhar melhor nossos gastos e rendimentos para chegar lá",
        "$VALUE_PLACEHOLDER?? Oloco hein, vamos nessa e conquistar esse objetivo!",
        "É isso ai temos que nos desafiar, com certeza vamos conseguir $VALUE_PLACEHOLDER",
        "Show show show! Essa meta $VALUE_PLACEHOLDER vai nos ajudar a ter mais organização com nossos gastos",
        "É isso ai vamos fazer o máximo para conquistar $VALUE_PLACEHOLDER!"
    )



}