package com.ilustris.alicia.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.messages.data.model.Action
import com.ilustris.alicia.ui.theme.toolbarColor


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SheetInput(action: Action, placeHolder: String, onConfirmClick: (String, Action) -> Unit) {


    Column(modifier = Modifier
        .background(color = toolbarColor(isSystemInDarkTheme()))
        .padding(16.dp)
        ) {
        var message by remember {
            mutableStateOf("")
        }

        val keyboardController = LocalSoftwareKeyboardController.current


        TextField(
            value = message,
            onValueChange = { message = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = getKeyboardType(action),
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onConfirmClick(message, action)
                    message = ""
                }

            ),
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.W700,
                textAlign = TextAlign.Center
            ),
            placeholder = {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = placeHolder,
                    color = Color.Gray.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
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
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),

            onClick = {
                keyboardController?.hide()
                onConfirmClick(message, action)
                message = ""
            }) {
            Text(text = "Confirmar", modifier = Modifier.padding(8.dp))
        }
    }
}

fun getKeyboardType(action: Action): KeyboardType {
    return when (action) {
        Action.GAIN, Action.LOSS, Action.GOAL -> KeyboardType.Decimal
        Action.NAME -> KeyboardType.Text
        else -> KeyboardType.Text
    }
}

@Preview(showBackground = true)
@Composable
fun sheetPreview() {
    SheetInput(
        action = Action.NAME,
        placeHolder = "Como devo lhe chamar?",
        onConfirmClick = { message, action ->
        })
}