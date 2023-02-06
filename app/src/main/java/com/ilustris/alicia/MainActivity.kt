package com.ilustris.alicia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilustris.alicia.features.finnance.ui.StatementScreen
import com.ilustris.alicia.features.home.ui.ChatScreen
import com.ilustris.alicia.ui.theme.AliciaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AliciaTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "chat") {
                    composable("chat") {
                        ChatScreen(
                            title = getString(R.string.app_name),
                            navController
                        )
                    }
                    composable("statement/{tag}") {
                        StatementScreen(navController = navController)
                    }
                }
            }
        }
    }
}



