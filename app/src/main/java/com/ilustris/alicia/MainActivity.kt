package com.ilustris.alicia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ilustris.alicia.features.finnance.ui.GoalScreen
import com.ilustris.alicia.features.finnance.ui.StatementScreen
import com.ilustris.alicia.features.home.ui.MainScreen
import com.ilustris.alicia.features.messages.ui.ChatScreen
import com.ilustris.alicia.ui.theme.AliciaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AliciaTheme {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(navController = navController, startDestination = MAIN_SCREEN) {
                    composable(CHAT_SCREEN) {
                        ChatScreen(
                            title = getString(R.string.app_name),
                            navController
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
                navController.navigate(CHAT_SCREEN)
            }
        }
    }
}



