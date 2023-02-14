package com.ilustris.alicia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ilustris.alicia.features.finnance.ui.GoalScreen
import com.ilustris.alicia.features.finnance.ui.StatementScreen
import com.ilustris.alicia.features.home.ui.MainScreen
import com.ilustris.alicia.features.messages.ui.ChatScreen
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AliciaTheme {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = MAIN_SCREEN,
                    modifier = Modifier.background(
                        toolbarColor(isSystemInDarkTheme())
                    )
                ) {
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

                LaunchedEffect(Unit) {
                    navController.navigate(CHAT_SCREEN)
                }
            }
        }
    }
}



