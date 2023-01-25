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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
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
import com.ilustris.alicia.features.messages.data.datasource.MessagePresets
import com.ilustris.alicia.features.messages.data.datasource.SuggestionsPresets
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.ui.theme.AliciaTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MessageSuggestion(
    suggestion: Suggestion,
    onSelectSuggestion: (Suggestion, String?) -> Unit
) {


    when (suggestion.type) {
        Type.NAME -> inputSuggestion(
            suggestion = suggestion,
            onSelectSuggestion = onSelectSuggestion
        )
        else -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                FilledIconButton(
                    onClick = { onSelectSuggestion(suggestion, null) },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = getSuggestionButtonColor(suggestion.type)
                    )
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = getSuggestionIcon(suggestion.type)),
                        contentDescription = "enviar",
                    )
                }
                Text(
                    text = suggestion.name,
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onBackground),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

fun getSuggestionIcon(type: Type): Int {
    return when (type) {
        Type.GAIN -> R.drawable.iconmonstr_coin_8
        Type.LOSS -> R.drawable.iconmonstr_download_10
        Type.GOAL -> R.drawable.iconmonstr_target_4
        Type.HISTORY -> R.drawable.iconmonstr_time_4
        else -> R.drawable.girl_face
    }
}

fun getSuggestionButtonColor(type: Type): Color {
    return when (type) {
        Type.GAIN -> MaterialColor.GreenA100
        Type.LOSS -> MaterialColor.RedA100
        Type.GOAL -> MaterialColor.OrangeA100
        Type.HISTORY -> MaterialColor.BlueGray200
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
            .wrapContentHeight()
            .border(1.dp, MaterialColor.Gray200, shape = RoundedCornerShape(25.dp))

    )
}

@Preview(showBackground = true)
@Composable
fun suggestionPreview() {
    return AliciaTheme() {
        Column(modifier = Modifier.padding(16.dp)) {
            MessageSuggestion(
                SuggestionsPresets.commonSuggestions().first(),
                onSelectSuggestion = { suggestion, value ->
                })
            MessageSuggestion(
                SuggestionsPresets.newUserSuggestions.first(),
                onSelectSuggestion = { suggestion, value ->
                })
        }

    }
}