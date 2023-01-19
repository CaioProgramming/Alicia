package com.ilustris.alicia.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    icon: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        Text(
            text = title,
            style = MaterialTheme
                .typography
                .headlineMedium
                .copy(color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.W600),
            modifier = Modifier.padding(16.dp)
        )


    }
}

@Preview(name = "TopBarr")
@Composable
private fun PreviewTopBarr() {
    TopBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface),
        icon = com.ilustris.alicia.R.drawable.girl,
        title = "Alicia"
    )
}