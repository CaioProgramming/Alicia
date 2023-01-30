package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.utils.formatToCurrencyText

@Composable
fun AmountComponent(amount: Double) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()
    ) {
        Text(
            text = "Seu saldo é",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.onBackground,
            )
        )
        Text(
            text = amount.formatToCurrencyText(),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Preview
@Composable
fun amountPreview() {
    AmountComponent(amount = 500.00)
}