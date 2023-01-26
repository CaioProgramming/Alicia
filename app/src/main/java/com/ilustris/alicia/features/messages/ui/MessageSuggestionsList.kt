package com.ilustris.alicia.features.messages.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.features.messages.ui.components.MessageSuggestion

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageSuggestionsList(
    modifier: Modifier,
    suggestions: State<List<Suggestion>>,
    onSelectSuggestion: (Suggestion, String?) -> Unit
) {

    val suggestions = remember { suggestions }

    val gridCells = if (suggestions.value.size == 1) StaggeredGridCells.Fixed(1) else StaggeredGridCells.Adaptive(170.dp)

    if (suggestions.value.size == 1) {
        Row(modifier = modifier) {
            MessageSuggestion(
                suggestion = suggestions.value.first(),
                onSelectSuggestion = onSelectSuggestion
            )
        }

    } else {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {

            items(suggestions.value) {
                MessageSuggestion(suggestion = it, onSelectSuggestion = onSelectSuggestion)
            }
        }
    }

}