package com.ilustris.alicia.features.home.ui.components

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import com.ilustris.alicia.R
import com.ilustris.alicia.ui.theme.AliciaTheme

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes icon: Int,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Image(
            painterResource(id = icon),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp, 70.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(4.dp)
        )

        Text(
            text = title,
            style = MaterialTheme
                .typography
                .bodyMedium
                .copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(8.dp)
        )



    }
}

@Preview(name = "TopBar")
@Composable
private fun PreviewTopBarr() {
    AliciaTheme {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface),
            icon = R.drawable.girl,
            title = "Alicia"
        )
    }

}