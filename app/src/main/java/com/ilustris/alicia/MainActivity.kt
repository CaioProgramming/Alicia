package com.ilustris.alicia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilustris.alicia.core.navigation.Routes
import com.ilustris.alicia.core.theme.AliciaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AliciaTheme {
                val navController = rememberNavController()

                val currentRoute by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)
                val currentNavRoute =
                    remember(currentRoute) {
                        currentRoute?.destination?.route?.let { Routes.valueOf(it) } ?: Routes.HOME
                    }

                Scaffold(topBar = {
                    TopAppBar(
                        backgroundColor = MaterialTheme.colorScheme.background,
                        elevation = 1.dp,
                        title = {
                            Text(
                                stringResource(currentNavRoute.title),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        },
                    )
                }) { _ ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME.name,
                    ) {
                        Routes.entries.forEach { route ->

                            composable(route.name) {
                                route.screen(navController)
                            }
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    navController.navigate(Routes.CHAT.name)
                }

                LaunchedEffect(navController.currentDestination) {
                    Log.i(javaClass.simpleName, "Current destination: ${navController.currentDestination?.route}")
                }
            }
        }
    }
}
