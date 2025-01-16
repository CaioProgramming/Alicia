package com.ilustris.alicia.utils

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Date

fun Double.formatToCurrencyText(showCurrency: Boolean = true): String {
    val currency = if (showCurrency) "R$" else ""
    return String.format("$currency%,.2f", this)
}

fun Modifier.bottomBorder(
    strokeWidth: Dp,
    color: Color,
) = composed(
    factory = {
        val density = LocalDensity.current

        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx,
            )
        }
    },
)

fun Modifier.topBorder(
    strokeWidth: Dp,
    color: Color,
) = composed(
    factory = {
        val density = LocalDensity.current

        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx,
            )
        }
    },
)
fun Color.darker(factor: Float = 0.7f): Color {
    return Color(
        red = (red * factor).coerceIn(0f, 1f),
        green = (green * factor).coerceIn(0f, 1f),
        blue = (blue * factor).coerceIn(0f, 1f),
        alpha = alpha
    )
}

fun Long.toDate() : Date {
    return Date(this)
}

fun Context.readAssetFile(fileName: String): String {
    val assetManager = this.assets
    val inputStream = assetManager.open(fileName)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    return bufferedReader.use { it.readText() }
}