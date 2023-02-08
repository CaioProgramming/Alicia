package com.ilustris.alicia.features.home.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Tag

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmojiSheet(onSelectTag: (Tag) -> Unit) {
    val tags = Tag.values().sortedBy { it.description }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Text(
            text = "Categoria",
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.Center
        ) {
            items(tags) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = it.emoji,
                        modifier = Modifier
                            .wrapContentSize()
                            .wrapContentSize()
                            .clickable {
                                onSelectTag(it)
                            },
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge

                    )

                    Text(
                        text = it.description,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall.copy(MaterialTheme.colorScheme.onBackground),
                        fontWeight = FontWeight.W300,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun EmojiPreview() {
    EmojiSheet {

    }
}