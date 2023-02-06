package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.messages.ui.components.StatementComponent
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.formatToCurrencyText
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CardStatement(tag: Tag, movimentations: List<Movimentation>, openStatement: (Int) -> Unit) {

    val spendValue = movimentations.sumOf { it.value }.formatToCurrencyText()

    var rotated by remember { mutableStateOf(false) }

    val cardDescription =
        if (movimentations.none { it.value < 0 }) "de rendimento na categoria." else "de gastos na categoria."

    val rotation by animateFloatAsState(
        targetValue = if (!rotated) 180f else 0f,
        animationSpec = tween(500)
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(500)
    )

    val animateBack by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(500)
    )
    Card(shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)),
        colors = CardDefaults.cardColors(
            containerColor = if (!rotated) toolbarColor(
                isSystemInDarkTheme()
            ) else MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(200.dp)
            .combinedClickable(
                onClick = {
                    rotated = !rotated
                },
                onDoubleClick = {
                    openStatement(tag.ordinal)
                },
                onLongClick = {
                    openStatement(tag.ordinal)
                }
            )
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }) {
        if (!rotated) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 40.dp)
                    .graphicsLayer {
                        rotationY = rotation
                    }
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
        } else {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = if (rotated) animateBack else animateFront
                }) {
                items(movimentations) {
                    StatementComponent(
                        it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer { rotationY = rotation })
                }
            }
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