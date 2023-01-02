package com.ilustris.alicia.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.HomeUI
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.smarttoolfactory.extendedcolors.ColorSwatch

@Composable
fun MessageBubble(message: String) {
    Card(
        shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 15.dp),
        modifier = Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = ColorSwatch.deepPurple[300]!!)
    ) {

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.padding(Dp(8f))

        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AliciaTheme {
        MessageBubble(message = "Fali comigo bebe")
    }
}