package com.ilustris.alicia.features.messages.ui.components

import ai.atick.material.MaterialColor
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.formatToCurrencyText

@Composable
fun StatementComponent(description: String, value: Double) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Text(
                text = description,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = value.formatToCurrencyText(),
                fontWeight = FontWeight.W600,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
        ) {
        }
    }
}

@Preview(showBackground = false)
@Composable
fun defaultPreview() {
    StatementComponent(description = "Nike", value = 150.00)
}