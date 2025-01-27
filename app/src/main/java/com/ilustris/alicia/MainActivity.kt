package com.ilustris.alicia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
                val currentDestination = navController.currentDestination?.route
                val backStackEntry by navController.currentBackStackEntryFlow.collectAsState(null)
                Scaffold(topBar = {
                    TopAppBar(
                        backgroundColor = MaterialTheme.colors.surface,
                        elevation = 1.dp,
                        title = {
                            Text(stringResource(R.string.app_name), color = MaterialTheme.colors.onBackground)
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colors.onBackground,
                                )
                            }
                        },
                    )
                }) { _ ->
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
