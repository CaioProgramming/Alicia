package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.messages.ui.components.StatementComponent
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.utils.formatToCurrencyText
import java.util.*

@Composable
fun StatementPage(movimentationInfo: MovimentationInfo) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = movimentationInfo.tag.emoji,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = TextUnit(
                    40f,
                    TextUnitType.Sp
                )
            ),
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = movimentationInfo.tag.description,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.W600
            )
        )
        Text(
            text = movimentationInfo.movimentations.sumOf { it.value }.formatToCurrencyText(true),
            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.padding(8.dp)
        )
        Card(
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            backgroundColor = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(movimentationInfo.movimentations) {
                    StatementComponent(
                        movimentation = it,
                        textColor = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun StatementPagePreview() {
    AliciaTheme {
        StatementPage(
            movimentationInfo = MovimentationInfo(
                Tag.GIFTS,
                listOf(
                    Movimentation(
                        value = 500.0,
                        description = "Nike Air",
                        spendAt = Random().nextLong()
                    )
                )
            )
        )
    }
}