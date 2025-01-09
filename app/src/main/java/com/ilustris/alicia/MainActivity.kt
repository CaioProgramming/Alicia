package com.ilustris.alicia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilustris.alicia.core.theme.AliciaTheme
import com.ilustris.alicia.features.finnance.ui.GoalScreen
import com.ilustris.alicia.features.finnance.ui.StatementScreen
import com.ilustris.alicia.features.home.ui.MainScreen
import com.ilustris.alicia.features.messages.ui.ChatScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AliciaTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = MAIN_SCREEN,
                ) {
                    composable(CHAT_SCREEN) {
                        ChatScreen(
                            navController = navController,
                        )
                    }
                    composable(STATEMENT_SCREEN) {
                        StatementScreen(navController)
                    }
                    composable(GOAL_SCREEN) {
                        GoalScreen(navController)
                    }

                    composable(MAIN_SCREEN) {
                        MainScreen(navController)
                    }
                }

                LaunchedEffect(Unit) {
                    navController.navigate(CHAT_SCREEN)
                }

                LaunchedEffect(navController.currentDestination) {
                    Log.i(javaClass.simpleName, "Current destination: ${navController.currentDestination?.route}")
                }
            }
        }
    }
}
