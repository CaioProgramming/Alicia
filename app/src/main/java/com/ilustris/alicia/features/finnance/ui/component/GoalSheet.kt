package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.alpha
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

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(bottom = 12.dp)
    ) {

        stickyHeader {
            Row(
                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                AnimatedVisibility(
                    currentGoal.value.id != 0,
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Button(
                        { onDelete() },
                        modifier = Modifier, colors =
                            ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                    ) {
                        Text("Remover")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    { onSaveGoal(currentGoal.value) },
                    modifier = Modifier, colors =
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
                textAlign = TextAlign.Center
            )
        }

        item() {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(15.dp))
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GoalMedalV3(
                    goal = currentGoal.value,
                    showText = false,
                    isAnimated = true,
                    modifier = Modifier
                        .size(150.dp)
                )

                TextField(
                    value = currentGoal.value.name,
                    onValueChange = { currentGoal.value = currentGoal.value.copy(name = it) },
                    colors = textFieldColors,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center
                    ),
                    placeholder = {
                        Text(
                            "Nome da Meta",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(.5f)
                        )
                    }
                )



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
                    placeholder = { Text("R\$ 0,00") },
                    colors = textFieldColors
                )
            }

        }


        item() {
            Text(
                "Categoria",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }


        item {
            TagIconsCard(
                Modifier.fillMaxWidth(),
                currentGoal.value.name,
                currentGoal.value.tag.findTag(),
                onUpdateTag = {
                    currentGoal.value = currentGoal.value.copy(tag = it.name)
                },
                onBadgeSelect = {
                    currentGoal.value = currentGoal.value.copy(badge = it)
                }
            )
        }

        item {
            Button(
                { onSaveGoal(currentGoal.value) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }

    }

}

@Composable
fun TagIconsCard(
    modifier: Modifier = Modifier,
    title: String,
    tag: Tag,
    onUpdateTag: (Tag) -> Unit = {},
    onBadgeSelect: (Int) -> Unit = {}
) {


    Column(
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(15.dp))
            .border(2.dp, tag.tagGradient(true), RoundedCornerShape(15.dp))
            .padding(16.dp)
    ) {

        TagPicker(tag = tag) {
            onUpdateTag(it)
        }


        Text(
            "Medalhas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(
                vertical = 8.dp
            )
        )

        LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
            items(tag.badges().size) { index ->
                GoalMedalV3(
                    goal = Goal(
                        name = title,
                        badge = index,
                        tag = tag.name
                    ),
                    showText = true,
                    isAnimated = true,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onBadgeSelect(index)
                        }
                        .size(100.dp)
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
            goal = Goal(name = "", value = 1000.0, id = 2, tag = Tag.TRAVEL.name),
            onSaveGoal = {}
        )
    }

}
