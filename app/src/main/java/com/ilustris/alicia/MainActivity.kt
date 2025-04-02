package com.ilustris.alicia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilustris.alicia.core.navigation.Routes
import com.ilustris.alicia.core.theme.AliciaTheme
import com.ilustris.alicia.core.theme.themeBrush
import com.ilustris.alicia.utils.emptyString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AliciaTheme {
                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()
                val currentRoute by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)
                val currentNavRoute =
                    remember(currentRoute) {
                        currentRoute?.destination?.route?.let { Routes.valueOf(it) } ?: Routes.HOME
                    }
                val user by viewModel.user.collectAsState(null)

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
                            AnimatedVisibility(currentNavRoute != Routes.HOME) {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(
                                        Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                        contentDescription = "Back",
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                        },
                        actions = {
                            AnimatedVisibility(user != null) {
                                Text(
                                   text = user?.name?.first().toString(),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        brush = themeBrush(),
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(50.dp)
                                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                                        .align(Alignment.CenterVertically)
                                        .padding(8.dp)
                                    )
                            }
                        }
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
                    viewModel.checkMovimentations()
                }
            }
        }
    }
}
