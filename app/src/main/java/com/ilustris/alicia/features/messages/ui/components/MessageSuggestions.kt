package com.ilustris.alicia.features.messages.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.ui.theme.AliciaTheme

@Composable
fun MessageSuggestion(
    message: String, value: Double,
    onSelectSuggestion: (Double) -> Unit
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier.padding(4.dp),onClick = {
        onSelectSuggestion(value)
    },
        shape = RoundedCornerShape(10.dp)) {
        Text(
            text = message,
            style = MaterialTheme.typography.labelSmall
        )
    }


}

@Preview
@Composable
fun suggestionPreview() {
    AliciaTheme() {
        val context = LocalContext.current
        MessageSuggestion(message = "Ganhei R$ 100,00", value = 100.0, onSelectSuggestion = {
            Toast.makeText(context, "Selecionou valor ${it}", Toast.LENGTH_SHORT).show()
        })
    }
}