package com.ilustris.alicia

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.ui.components.MessageBubble
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.smarttoolfactory.extendedcolors.ColorSwatch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AliciaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeUI(title = "Alicia app")
                }
            }
        }
    }
}

@Composable
fun HomeUI(title: String) {
    Column(Modifier.padding(Dp(16f))) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Seu app para organizar suas finanças de um jeitinho diferente",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
        MessageBubble(message = "Me conte todos os seus segredos rsrs")
        Greeting("Oi, eu sou a Alicia, vamos economizar?")

    }
}


@Composable
fun Greeting(name: String) {
    val context = LocalContext.current
    Button(content = { Text(text = name, color = Color.White) }, onClick = {
        Toast.makeText(context, "Compondo coisas por ai", Toast.LENGTH_SHORT).show()
    })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AliciaTheme {
        HomeUI(title = "Alicia app")
    }
}