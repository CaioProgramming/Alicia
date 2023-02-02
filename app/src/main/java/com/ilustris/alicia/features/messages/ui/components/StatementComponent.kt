package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import com.ilustris.alicia.utils.formatToCurrencyText
import java.util.*
import kotlin.random.Random

@Composable
fun StatementComponent(movimentation: Movimentation, modifier: Modifier) {
    var visible by remember {
        mutableStateOf(true).apply {
            this.value = true
        }
    }
    val formattedDate = Calendar.getInstance().apply {
        timeInMillis = movimentation.spendAt
    }.time.format(DateFormats.DD_MM_YYY)
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(animationSpec = tween(1500)),
        exit = shrinkVertically(animationSpec = tween(1500))
    ) {

    }
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                val endIndex = minOf(movimentation.description.length, 10)
                Text(
                    text = movimentation.description.substring(0, endIndex)
                        .replaceRange(endIndex - 3, endIndex, "..."),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.W800,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSecondary,
                    maxLines = 2,
                )
                Text(
                    text = movimentation.tag.description,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.W400,
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f),
                    maxLines = 1,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = movimentation.value.formatToCurrencyText(),
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    text = formattedDate,
                    fontWeight = FontWeight.W300,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))
        ) {
        }
    }
}

@Preview(showBackground = false)
@Composable
fun defaultPreview() {
    StatementComponent(
        Movimentation(
            description = "Nike Air 9251",
            value = 150.00,
            tag = Tag.BILLS,
            spendAt = Random.nextLong()
        ), Modifier.fillMaxWidth()
    )
}