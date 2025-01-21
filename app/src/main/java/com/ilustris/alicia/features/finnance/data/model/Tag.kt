package com.ilustris.alicia.features.finnance.data.model

import ai.atick.material.MaterialColor
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.ilustris.alicia.R
import com.ilustris.alicia.utils.darker
import com.ilustris.alicia.utils.gradientAnimation

enum class Tag(
    val emoji: String,
    val description: String,
    val colors: List<Color> = emptyList(),
    @DrawableRes
    val icon: Int = R.drawable.ic_round_star_24,
    val textColor: Color = colors.last().darker(0.9f),
) {
    PETS(
        "🐶",
        "Pets",
        listOf(
            Color(0xFFFCE4EC),
            Color(0xFFF8BBD0),
            Color(0xFFEF9A9A),
        ),
        R.drawable.ic_pets_24,
    ),
    WORK(
        "💼",
        "Trabalho",
        listOf(
            Color(0xFF3F51B5),
            Color(0xFF283593),
            Color(0xFF2979FF),
        ),
    ),
    GROCERIES(
        "🍕",
        "Alimentação",
        listOf(
            Color(0xFFA5D6A7),
            Color(0xFF66BB6A),
            Color(0xFF43A047),
        ),
        R.drawable.ic_restaurant_menu_24,
    ),
    HEALTH(
        "💊",
        "Saúde",
        listOf(
            Color(0xFFFF8A80),
            Color(0xFFC2185B),
            Color(0xFFF50057),
        ),
    ),
    BILLS(
        "🪙",
        "Contas",
        listOf(
            Color(0xFFFFE082),
            Color(0xFFFFCA28),
            Color(0xFFFFB300),
        ),
    ),
    TRANSPORT(
        "🚗",
        "Transporte",
        listOf(
            Color(0xFFFFCC80),
            Color(0xFFFFA726),
            Color(0xFFFB8C00),
        ),
        R.drawable.ic_race_flag,
    ),
    EDUCATION(
        "📓",
        "Educação",
        listOf(
            Color(0xFF9FA8DA),
            Color(0xFF5C6BC0),
            Color(0xFF3949AB),
        ),
    ),
    ENTERTAINMENT(
        "🍿",
        "Entretenimento",
        listOf(
            MaterialColor.Cyan500,
            MaterialColor.Yellow300,
            MaterialColor.BlueGray200,
            MaterialColor.PinkA400,
            MaterialColor.Blue500
        ),
        textColor = Color.Black,
        icon = R.drawable.bill_badge_1
    ),
    TRAVEL(
        "✈️",
        "Viagem",
        listOf(
            Color(0xFF80CBC4),
            Color(0xFF26A69A),
            Color(0xFF00897B),
        ),
    ),
    SHOPPING(
        "🛍️",
        "Compras",
        listOf(
            Color(0xFFFFF59D),
            Color(0xFFFFEB3B),
            Color(0xFFFDD835),
        ),
    ),
    GAMES(
        "🎮",
        "Jogos",
        listOf(
            MaterialColor.PurpleA700,
            MaterialColor.Purple300,
            MaterialColor.Blue900,
            MaterialColor.Orange100,
            MaterialColor.LightBlue400
        ),
    ),

    UNKNOWN(
        "❓",
        "Desconhecido",
        listOf(
            MaterialColor.Yellow200,
            MaterialColor.Yellow400,
            MaterialColor.YellowA700,
            MaterialColor.YellowA400,
            MaterialColor.DeepOrange500
        ),
    ),
    ;

    @Composable
    fun tagGradient(isAnimated: Boolean = false) =
        if (isAnimated) {
            Brush.verticalGradient(
                colors = colors,
            )
        } else {
            gradientAnimation(colors)
        }

    fun badges() =
        when (this) {
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

    fun shape() = TagHelper.tagShape(this)
}

fun String?.findTag(): Tag {
    if (this == null) return Tag.UNKNOWN
    return Tag.entries.find { it.name == this } ?: Tag.UNKNOWN
}


