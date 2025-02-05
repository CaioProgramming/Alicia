package com.ilustris.alicia.features.home.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.core.theme.AliciaTheme
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.data.model.findTag
import com.ilustris.alicia.utils.CurrencyVisualTransformation
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import com.ilustris.alicia.utils.formatToCurrencyText
import com.ilustris.alicia.utils.toDate
import java.util.Calendar

@Composable
fun MovimentationSheet(
    title: String,
    sheetDescription: String,
    movimentation: Movimentation? = null,
    onConfirmClick: (Movimentation) -> Unit,
) {
    var spendValue by remember {
        mutableStateOf(movimentation?.value?.toString() ?: "")
    }

    var description by remember {
        mutableStateOf(movimentation?.description ?: "")
    }

    var tag by remember {
        mutableStateOf(movimentation?.tag?.findTag() ?: Tag.UNKNOWN)
    }

    var categoryVisible by remember {
        mutableStateOf(movimentation != null)
    }

    val focusRequester = remember { FocusRequester() }

    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDate =
        remember { mutableLongStateOf(movimentation?.spendAt ?: Calendar.getInstance().timeInMillis) }

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Text(
            title,
            style =
                MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        )

        Text(
            sheetDescription,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            modifier = Modifier.fillMaxWidth(),
        )

        TextField(
            value = spendValue,
            onValueChange = {
                spendValue =
                    if (it.startsWith("0")) {
                        ""
                    } else {
                        it
                    }
            },
            visualTransformation = CurrencyVisualTransformation(),
            colors = textFieldColors,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = getKeyboardType(),
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                ),
            keyboardActions =
                KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        categoryVisible = !categoryVisible
                    },
                ),
            textStyle =
                MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Center,
                ),
            placeholder = {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = 0.toDouble().formatToCurrencyText(false),
                    color = Color.Gray.copy(alpha = 0.5f),
                )
            },
            singleLine = true,
            modifier =
                Modifier
                    .animateContentSize()
                    .background(Color.Transparent),
        )

        Row(
            modifier =
                Modifier
                    .wrapContentWidth()
                    .background(tag.textColor, RoundedCornerShape(25.dp))
                    .padding(8.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .clickable {
                        categoryVisible = !categoryVisible
                    },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = tag.description,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.W500,
            )
            Image(
                painterResource(id = tag.icon),
                colorFilter = ColorFilter.tint(tag.textColor),
                contentDescription = null,
                modifier =
                    Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                        .padding(4.dp),
            )
        }

        AnimatedVisibility(
            visible = categoryVisible,
            enter = expandVertically(tween(500)),
            exit = shrinkVertically(tween(500)),
        ) {
            EmojiSheet(onSelectTag = {
                tag = it
                categoryVisible = false
            })
        }

        Row(
            modifier =
                Modifier
                    .padding(4.dp)
                    .clickable {
                        showDatePicker = true
                    }.clip(RoundedCornerShape(5.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onBackground.copy(alpha = .2f),
                        RoundedCornerShape(5.dp),
                    ).fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Rounded.DateRange, contentDescription = null)
            Text(
                text = selectedDate.longValue.toDate().format(DateFormats.DD_OF_MM_FROM_YYYY),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        TextField(
            value = description,
            onValueChange = { description = it },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                ),
            keyboardActions =
                KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
            textStyle =
                MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            colors = textFieldColors,
            placeholder = {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Descrição",
                    color = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
                    .focusRequester(focusRequester),
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            enabled = description.isNotEmpty() && spendValue.isNotEmpty(),
            onClick = {
                val movimentationDescription = description
                val movimentationValue = spendValue
                val movimentationTag = tag

                keyboardController?.hide()
                val newMovimentation =
                    Movimentation(
                        value = movimentationValue.toDouble(),
                        description = movimentationDescription,
                        tag = movimentationTag.name,
                        spendAt = selectedDate.longValue,
                    )
                onConfirmClick(newMovimentation)
                spendValue = ""
                description = ""
            },
        ) {
            Text(text = "Confirmar", modifier = Modifier.padding(8.dp))
        }
    }

    if (showDatePicker) {
        DatePickerModal({ date ->
            date?.let {
                selectedDate.longValue = it
            }
        }) {
            showDatePicker = false
        }
    }
}

fun getKeyboardType(): KeyboardType = KeyboardType.NumberPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview(showBackground = true)
@Composable
fun SheetPreview() {
    AliciaTheme {
        MovimentationSheet(
            title = "Adicionar despesa",
            sheetDescription = "Adicione uma nova despesa",
            movimentation =
                Movimentation(
                    value = 0.50,
                    description = "Teste",
                    tag = Tag.PETS.name,
                    spendAt = System.currentTimeMillis(),
                ),
            onConfirmClick = { },
        )
    }
}
