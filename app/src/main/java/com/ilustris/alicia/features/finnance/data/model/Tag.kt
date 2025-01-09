package com.ilustris.alicia.features.finnance.data.model

import ai.atick.material.MaterialColor
import androidx.compose.ui.graphics.Color

enum class Tag(
    val emoji: String,
    val description: String,
    val color: Color,
) {
    PETS("🐶", "Pets", MaterialColor.Brown200),
    WORK("💼", "Trabalho", MaterialColor.Brown700),
    GROCERIES("🍕", "Alimentação", MaterialColor.Pink400),
    HEALTH("💊", "Saúde", MaterialColor.Green400),
    BILLS("🪙", "Contas", MaterialColor.Yellow500),
    TRANSPORT("🚗", "Transporte", MaterialColor.Gray800),
    EDUCATION("📓", "Educação", MaterialColor.BlueA200),
    ENTERTAINMENT("🍿", "Entretenimento", MaterialColor.RedA400),
    TRAVEL("✈️", "Viagem", MaterialColor.YellowA400),
    UNKNOWN("🤔", "Desconhecido", MaterialColor.Gray300),
    SHOPPING("🛍️", "Compras", MaterialColor.BlueA700),
    GAMES("🎮", "Jogos", MaterialColor.BlueGray600);

    fun badges() = when(this) {
        PETS -> TagHelper.petBadges
        TRANSPORT -> TagHelper.transportBadges
        SHOPPING -> TagHelper.shoppingBadges
        GAMES -> TagHelper.gameBadges
        TRAVEL -> TagHelper.travelBadges
        EDUCATION -> TagHelper.educationBadges
        WORK -> TagHelper.workBadges
        ENTERTAINMENT -> TagHelper.partyBadges
        GROCERIES -> TagHelper.groeceryBadges
        HEALTH -> TagHelper.healthBadges
        BILLS -> TagHelper.billsBadges
        else -> TagHelper.commonBadges
    }
}

fun String?.findTag(): Tag {
    if (this == null) return Tag.UNKNOWN
    return Tag.entries.find { it.name == this } ?: Tag.UNKNOWN
}
