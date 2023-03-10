package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        Action.NAME -> Box(modifier = Modifier.wrapContentSize())
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
                    text = suggestion.action.description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
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