package com.ilustris.alicia.features.messages.ui.components

import ai.atick.material.MaterialColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.R
import com.ilustris.alicia.features.messages.data.datasource.SuggestionsPresets
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.ui.theme.AliciaTheme

@Composable
fun MessageSuggestion(
    suggestion: Suggestion,
    onSelectSuggestion: (Suggestion, String?) -> Unit
) {


    when (suggestion.action) {
        Action.NAME -> inputSuggestion(
            suggestion = suggestion,
            onSelectSuggestion = onSelectSuggestion
        )
        else -> {
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        1.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                onClick = { onSelectSuggestion(suggestion, null) }) {
                Text(
                    text = suggestion.name,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun getSuggestionIcon(action: Action): Int {
    return when (action) {
        Action.PROFIT -> R.drawable.iconmonstr_coin_8
        Action.LOSS -> R.drawable.iconmonstr_download_10
        Action.GOAL -> R.drawable.iconmonstr_target_4
        Action.HISTORY -> R.drawable.iconmonstr_time_4
        else -> R.drawable.girl_face
    }
}

fun getSuggestionButtonColor(action: Action): Color {
    return when (action) {
        Action.PROFIT -> MaterialColor.GreenA100
        Action.LOSS -> MaterialColor.RedA100
        Action.GOAL -> MaterialColor.OrangeA100
        Action.HISTORY -> MaterialColor.BlueGray200
        else -> MaterialColor.Purple300
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun inputSuggestion(suggestion: Suggestion, onSelectSuggestion: (Suggestion, String?) -> Unit) {
    var message by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = message,
        onValueChange = { message = it },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Words,
            autoCorrect = false,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onSelectSuggestion(suggestion, message)
                message = ""
            }

        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.W400,
        ),
        placeholder = {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = suggestion.name,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
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
        trailingIcon = {
            IconButton(
                onClick = {
                    onSelectSuggestion(suggestion, message)
                    message = ""
                },
                colors = IconButtonDefaults
                    .iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_send_24),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                    contentDescription = "enviar",
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight()
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground.copy(0.5f),
                shape = RoundedCornerShape(25.dp)
            )

    )
}

@Preview(showBackground = true)
@Composable
fun suggestionPreview() {
    return AliciaTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MessageSuggestion(
                SuggestionsPresets.commonSuggestions.first(),
                onSelectSuggestion = { suggestion, value ->
                })
            MessageSuggestion(
                SuggestionsPresets.newUserSuggestions.first(),
                onSelectSuggestion = { suggestion, value ->
                })
        }

    }
}