package com.ilustris.alicia.features.home.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.CurrencyInputTransformation


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun SheetInput(
    action: Action,
    placeHolder: String,
    title: String,
    focusRequester: FocusRequester,
    onConfirmClick: (String, String, Tag, Action) -> Unit
) {
    var spendValue by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var tag by remember {
        mutableStateOf(Tag.UNKNOWN)
    }

    var categoryVisible by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(color = toolbarColor(isSystemInDarkTheme()))
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {


        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        TextField(
            value = description,
            onValueChange = { description = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
            placeholder = {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = placeHolder,
                    color = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            label = {
                Text(
                    style = MaterialTheme.typography.bodySmall,
                    text = title,
                    color = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onBackground,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent,
                containerColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .focusRequester(focusRequester)
        )


        TextField(
            value = spendValue,
            onValueChange = {
                spendValue = if (it.startsWith("0")) {
                    ""
                } else {
                    it
                }
            },
            visualTransformation = CurrencyInputTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = getKeyboardType(action),
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    categoryVisible = !categoryVisible
                }

            ),
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.W700,
            ),
            placeholder = {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = placeHolder,
                    color = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            leadingIcon = {
                Text(
                    text = "R$", style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.W300,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onBackground,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent,
                containerColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),

            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),

            onClick = {
                categoryVisible = !categoryVisible
            }) {
            Text(text = tag.emoji)
            Text(text = tag.description)
        }

        AnimatedVisibility(
            visible = categoryVisible,
            enter = expandVertically(tween(500)),
            exit = shrinkVertically(tween(500))
        ) {
            EmojiSheet(onSelectTag = {
                tag = it
                categoryVisible = false
            })
        }


        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            enabled = description.isNotEmpty() && spendValue.isNotEmpty(),
            onClick = {
                val movimentationDescription = description
                val movimentationValue = spendValue
                val movimentationTag = tag
                spendValue = ""
                description = ""
                tag = Tag.UNKNOWN
                keyboardController?.hide()
                onConfirmClick(
                    movimentationDescription,
                    movimentationValue,
                    movimentationTag,
                    action
                )
            }) {
            Text(text = "Confirmar", modifier = Modifier.padding(8.dp))
        }
    }

}

fun getKeyboardType(action: Action): KeyboardType {
    return when (action) {
        Action.NAME -> KeyboardType.Text
        else -> KeyboardType.NumberPassword

    }
}

@Preview(showBackground = true)
@Composable
fun SheetPreview() {
    val focusRequester = remember { FocusRequester() }

    return SheetInput(
        action = Action.NAME,
        placeHolder = "0,00",
        title = "Nome da despesa",
        focusRequester = focusRequester,
        onConfirmClick = { description, message, tag, action ->
        })
}