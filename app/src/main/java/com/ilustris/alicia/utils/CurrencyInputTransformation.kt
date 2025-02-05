package com.ilustris.alicia.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Currency
import java.util.Locale
import kotlin.math.max
import kotlin.math.pow

class CurrencyVisualTransformation(
    private val locale: Locale = Locale.getDefault(),
    private val currencySymbol: String = DecimalFormatSymbols.getInstance(locale).currencySymbol,
    private val numberOfDecimalPlaces: Int = 2
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text.filter { it.isDigit() || it == '.' }
        val decimalNumber = inputText.toDoubleOrNull() ?: 0.0

        val decimalFormat = DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
        decimalFormat.decimalFormatSymbols = DecimalFormatSymbols(locale).apply {
            currencySymbol = ""
        }
        val formattedNumber = decimalFormat.format(decimalNumber)

        val newText = buildAnnotatedString {
            withStyle(SpanStyle(fontSize = 12.sp)) {
                append(currencySymbol)
            }
            append(formattedNumber)
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = newText.length
            override fun transformedToOriginal(offset: Int): Int = text.length
        }

        return TransformedText(newText, offsetMapping)
    }
}