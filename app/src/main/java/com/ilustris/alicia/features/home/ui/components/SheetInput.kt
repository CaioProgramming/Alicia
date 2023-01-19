package com.ilustris.alicia.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.messages.domain.model.Action


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetInput(action: Action, placeHolder: String, onConfirmClick: (String) -> Unit) {

    Column(modifier = Modifier.padding(16.dp)) {
        var message by remember {
            mutableStateOf("")
        }


        TextField(
            value = message,
            onValueChange = { message = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = getKeyboardType(action),
                imeAction = ImeAction.Send,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false
            ),
            textStyle = MaterialTheme.
            typography.
            headlineMedium.copy(fontWeight = FontWeight.W700,
                textAlign = TextAlign.Center),
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
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),

            onClick = {
                onConfirmClick(message)
            }) {
            Text(text = "Confirmar")
        }
    }
}

fun getKeyboardType(action: Action): KeyboardType {
   return when(action) {
        Action.GAIN,Action.LOSS, Action.GOAL -> KeyboardType.Decimal
        Action.NAME -> KeyboardType.Text
        else -> KeyboardType.Text
    }
}

@Preview(showBackground = true)
@Composable
fun sheetPreview() {
    SheetInput(action = Action.NAME,placeHolder = "Como devo lhe chamar?", onConfirmClick = {
    })
}