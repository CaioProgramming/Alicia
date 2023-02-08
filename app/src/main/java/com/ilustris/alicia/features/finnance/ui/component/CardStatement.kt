package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.formatToCurrencyText
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardStatement(tag: Tag, movimentations: List<Movimentation>, openStatement: (Int) -> Unit) {

    val spendValue = movimentations.sumOf { it.value }.formatToCurrencyText()


    val cardDescription =
        if (movimentations.none { it.value < 0 }) "de rendimento na categoria." else "de gastos na categoria."

    Card(shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)),
        colors = CardDefaults.cardColors(
            containerColor = toolbarColor(
                isSystemInDarkTheme()
            )
        ),
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(200.dp)
            .combinedClickable(
                onClick = {
                    openStatement(tag.ordinal)
                },
                onDoubleClick = {
                    openStatement(tag.ordinal)
                },
                onLongClick = {
                    openStatement(tag.ordinal)
                }
            )) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 40.dp)
                .fillMaxSize()
        ) {
            Text(text = tag.emoji, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = tag.description,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = spendValue,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground

            )
            Text(
                text = cardDescription,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.W300,
                color = MaterialTheme.colorScheme.onBackground

            )
        }

    }
}

@Preview
@Composable
fun statementPreview() {
    CardStatement(
        tag = Tag.BILLS, movimentations = listOf(
            Movimentation(
                value = 500.0,
                description = "Nike Air",
                spendAt = Random.nextLong()
            ),
            Movimentation(
                value = 500.0,
                description = "Nike Air",
                spendAt = Random.nextLong()
            ),
            Movimentation(
                value = 500.0,
                description = "Nike Air",
                spendAt = Random.nextLong()
            ),
            Movimentation(
                value = 500.0,
                description = "Nike Air",
                spendAt = Random.nextLong()
            ),
            Movimentation(
                value = 500.0,
                description = "Nike Air",
                spendAt = Random.nextLong()
            ),
            Movimentation(
                value = 500.0,
                description = "Nike Air",
                spendAt = Random.nextLong()
            ),
            Movimentation(
                value = 500.0,
                description = "Nike Air",
                spendAt = Random.nextLong()
            )
        ), openStatement = { }
    )
}