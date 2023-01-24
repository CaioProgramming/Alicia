package com.ilustris.alicia.features.messages.ui.components

import ai.atick.material.MaterialColor
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
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
import com.ilustris.alicia.features.home.ui.components.getKeyboardType
import com.ilustris.alicia.features.messages.data.datasource.MessagePresets
import com.ilustris.alicia.features.messages.data.model.Action
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.ui.theme.AliciaTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MessageSuggestion(
    suggestion: Suggestion,
    onSelectSuggestion: (Suggestion, String?) -> Unit
) {

    var message by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current


    when (suggestion.action) {
        Action.NONE,
        Action.GAIN,
        Action.LOSS,
        Action.GOAL,
        Action.USER,
        Action.HEADER -> Button(
            modifier = Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                onSelectSuggestion(suggestion, "")
            },
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                text = suggestion.name,
                style = MaterialTheme.typography.labelSmall
            )
        }
        else -> {
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
                    fontWeight = FontWeight.W700,
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
                        onClick = { onSelectSuggestion(suggestion, message) },
                        colors = IconButtonDefaults
                            .iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_send_24),
                            colorFilter =  ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
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
    }


}

@Preview(showBackground = true)
@Composable
fun suggestionPreview() {
   return AliciaTheme() {
        Column(modifier = Modifier.padding(16.dp)) {
            MessageSuggestion(
                MessagePresets.commonSuggestions().first(),
                onSelectSuggestion = { suggestion, value ->
                })
            MessageSuggestion(
                MessagePresets.newUserSuggestions.first(),
                onSelectSuggestion = { suggestion, value ->
                })
        }

    }
}