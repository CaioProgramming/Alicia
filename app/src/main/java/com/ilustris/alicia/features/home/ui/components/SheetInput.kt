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
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.CurrencyInputTransformation


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SheetInput(
    type: Type,
    placeHolder: String,
    title: String,
    focusRequester: FocusRequester,
    onConfirmClick: (String, String, Type) -> Unit
) {


    Column(
        modifier = Modifier
            .background(color = toolbarColor(isSystemInDarkTheme()))
            .padding(16.dp)
    ) {
        var spendValue by remember {
            mutableStateOf("")
        }

        var description by remember {
            mutableStateOf("")
        }



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
                .background(Color.Transparent).focusRequester(focusRequester)
        )

       /* LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        } */

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
                keyboardType = getKeyboardType(type),
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    val movimentationValue = spendValue
                    val movimentationDescription = description
                    onConfirmClick(movimentationDescription, movimentationValue, type)
                    spendValue = ""
                    description = ""
                    keyboardController?.hide()
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
            shape = RoundedCornerShape(5.dp),
            enabled = description.isNotEmpty() && spendValue.isNotEmpty(),
            onClick = {
                spendValue = ""
                description = ""
                keyboardController?.hide()
                onConfirmClick(description, spendValue, type)
            }) {
            Text(text = "Confirmar", modifier = Modifier.padding(8.dp))
        }
    }
}

fun getKeyboardType(type: Type): KeyboardType {
    return when (type) {
        Type.GAIN, Type.LOSS, Type.GOAL -> KeyboardType.NumberPassword
        Type.NAME -> KeyboardType.Text
        else -> KeyboardType.Text
    }
}

@Preview(showBackground = true)
@Composable
fun SheetPreview() {
    val focusRequester = remember { FocusRequester() }

   return SheetInput(
        type = Type.NAME,
        placeHolder = "0,00",
        title = "Nome da despesa",
        focusRequester = focusRequester,
        onConfirmClick = { description, message, action ->
        })
}