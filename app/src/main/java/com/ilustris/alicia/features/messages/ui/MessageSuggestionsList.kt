package com.ilustris.alicia.features.messages.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.features.messages.ui.components.MessageSuggestion

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageSuggestionsList(
    modifier: Modifier,
    suggestions: List<Suggestion>,
    onSelectSuggestion: (Suggestion, String?) -> Unit
) {

    val suggestions = remember { suggestions }

    suggestions.let { suggestionList ->
        if (suggestionList.size == 1) {
            Row(modifier = modifier) {
                MessageSuggestion(
                    suggestion = suggestionList.first(),
                    onSelectSuggestion = onSelectSuggestion
                )
            }

        } else {
            LazyRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                items(suggestionList) {
                    MessageSuggestion(suggestion = it, onSelectSuggestion = onSelectSuggestion)
                }

            }
        }

    }

}