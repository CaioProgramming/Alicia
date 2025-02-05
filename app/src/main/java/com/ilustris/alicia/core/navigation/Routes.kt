@file:OptIn(ExperimentalComposeUiApi::class)

package com.ilustris.alicia.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import com.ilustris.alicia.R
import com.ilustris.alicia.features.finnance.ui.GoalScreen
import com.ilustris.alicia.features.finnance.ui.StatementScreen
import com.ilustris.alicia.features.home.ui.MainScreen
import com.ilustris.alicia.features.messages.ui.ChatScreen

enum class Routes(
    val title: Int = R.string.app_name,
    val screen: @Composable (NavHostController) -> Unit,
) {
    HOME(R.string.home_title, screen = {
        MainScreen(it)
    }),
    CHAT(
        R.string.app_name,
        screen = {
            ChatScreen(navController = it)
        },
    ),
    STATEMENT(R.string.history_title, screen = {
        StatementScreen(navController = it)
    }),
    GOAL(R.string.goal_title, screen = {
        GoalScreen(it)
    }),
}
