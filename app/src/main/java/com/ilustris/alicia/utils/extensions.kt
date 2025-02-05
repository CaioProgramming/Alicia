package com.ilustris.alicia.utils

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.DecimalFormatSymbols
import java.util.Date

fun Double.formatToCurrencyText(showCurrency: Boolean = true): AnnotatedString {
    val currencySymbol = DecimalFormatSymbols.getInstance(java.util.Locale.getDefault()).currencySymbol
    val currency = if (showCurrency) currencySymbol else ""
    val formattedAmount = String.format("%,.2f", this)
    return buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 12.sp)) {
            append(currency)
        }
        append(formattedAmount)
    }
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

fun Color.darker(factor: Float = 0.7f): Color =
    Color(
        red = (red * factor).coerceIn(0f, 1f),
        green = (green * factor).coerceIn(0f, 1f),
        blue = (blue * factor).coerceIn(0f, 1f),
        alpha = alpha,
    )

fun Long.toDate(): Date = Date(this)

fun Context.readAssetFile(fileName: String): String {
    val assetManager = this.assets
    val inputStream = assetManager.open(fileName)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    return bufferedReader.use { it.readText() }
}
