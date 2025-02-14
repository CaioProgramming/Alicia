package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.core.theme.AliciaTheme
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.data.model.findTag
import com.ilustris.alicia.utils.CurrencyVisualTransformation
import com.ilustris.alicia.utils.formatToCurrencyText

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GoalSheet(
    title: String,
    subtitle: String?,
    goal: Goal? = null,
    onSaveGoal: (Goal) -> Unit,
    onDelete: () -> Unit = {}
) {
    var currentGoal = remember {
        mutableStateOf(goal ?: Goal())
    }




    val textFieldColors =
        TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        )

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally)  {

        stickyHeader {
            Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {

                AnimatedVisibility(currentGoal.value.id != 0) {
                    Button({onSaveGoal(currentGoal.value)},
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .align(Alignment.TopStart), colors =
                            ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                    ) {
                        Text("Remover")
                    }
                }

                Button({onSaveGoal(currentGoal.value)},
                    modifier = Modifier.padding(horizontal = 16.dp)
                    .align(Alignment.TopEnd), colors =
                ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("Salvar")
                }
            }
        }

        item {
            Text(
                text = title,
                style =
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.W700,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
            )
        }
        item() {
            Text(
                text = subtitle ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        item() {
             GoalMedalV3(
                 goal = currentGoal.value,
                 showText = false,
                 isAnimated = true,
                 modifier = Modifier.size(150.dp).clip(CircleShape)
             )
         }


        item() {
            TextField(
                value = currentGoal.value.name, onValueChange = { currentGoal.value = currentGoal.value.copy(name = it) },
                colors = textFieldColors,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = TextAlign.Center
                ),
                label = { Text("Nome da Meta") }
            )
        }

        item() {

            TagPicker(currentGoal.value.tag.findTag()) {
                currentGoal.value = currentGoal.value.copy(tag = it.name)
            }
        }


        item {
            TextField(
                currentGoal.value.toString(),
                onValueChange = {
                    currentGoal.value = currentGoal.value.copy(value = it.toDouble())
                },
                visualTransformation = CurrencyVisualTransformation(),
                textStyle =
                    MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.Center,
                    ),
                label = { Text("Valor da Meta") },
                colors = textFieldColors
            )
        }


        item() {
            Text("Ícones",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(16.dp))
        }


            item {
                TagIconsCard(
                    Modifier.fillMaxWidth(),
                    currentGoal.value.name,
                    currentGoal.value.tag.findTag(),
                    onBadgeSelect = {
                        currentGoal.value = currentGoal.value.copy(badge = it)
                    }
                )
            }


    }

}

@Composable
fun TagIconsCard(modifier: Modifier = Modifier, title: String, tag: Tag, onBadgeSelect: (Int) -> Unit = {}) {

    val badges = remember {
        tag.badges()
    }
    Column(modifier = modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(16.dp).background(MaterialTheme.colorScheme.background, RoundedCornerShape(15.dp))) {
        Text(
            text = tag.description,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)

        )

        LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
            items(badges.size) { index ->
                GoalMedalV3(
                    goal = Goal(
                        name = title,
                        badge = index,
                        tag = tag.name
                    ),
                    showText = true,
                    isAnimated = true,
                    modifier = Modifier.padding(8.dp).clip(CircleShape).clickable {
                        onBadgeSelect(index)
                    }.size(120.dp)
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GoalSheetPreview() {
    AliciaTheme {
        GoalSheet(
            title = "Nova Meta",
            subtitle = "Adicione uma nova meta",
            goal = Goal(name = "Meta 1", value = 1000.0, id = 2, tag = Tag.TRAVEL.name),
            onSaveGoal = {}
        )
    }

}
