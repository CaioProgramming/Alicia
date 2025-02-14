package com.ilustris.alicia.features.home.ui

import ai.atick.material.MaterialColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ilustris.alicia.R
import com.ilustris.alicia.core.navigation.Routes
import com.ilustris.alicia.core.theme.AliciaTheme
import com.ilustris.alicia.features.finnance.ui.StatementList
import com.ilustris.alicia.features.home.domain.model.Avatar
import com.ilustris.alicia.features.home.presentation.MainViewModel
import com.ilustris.alicia.features.home.ui.components.AvatarProfile

@Composable
fun MainScreen(navController: NavHostController) {
    val viewmodel: MainViewModel = hiltViewModel()
    val goals = viewmodel.goals.collectAsState(initial = emptyList())
    val movimentations = viewmodel.movimentations.collectAsState(initial = emptyList())
    val user = viewmodel.user.collectAsState(initial = null)
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        val avatars = ArrayList(getAvatars())
        if (goals.value.isEmpty()) {
            avatars.remove(avatars.find { it.redirect == Routes.GOAL.name })
        }
        if (movimentations.value.isEmpty()) {
            avatars.remove(avatars.find { it.redirect == Routes.STATEMENT.name })
        }
        user.value?.let {
            Text(
                text = "Olá, ${it.name}",
                style =
                    MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                    ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(avatars) {
                AvatarProfile(avatar = it) { direction ->
                    navController.navigate(direction)
                }
            }
        }
        if (movimentations.value.isNotEmpty()) {
            Text(
                text = "Últimas movimentações",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.W600,
                    ),
            )
            StatementList(movimentations = movimentations.value, onSelect = {
                navController.navigate(Routes.STATEMENT.name)
            })
        }
    }
}

@Composable
fun getAvatars() =
    listOf(
        Avatar(
            stringResource(R.string.history_girl_name),
            R.drawable.history_girl,
            MaterialColor.Purple700,
            Routes.STATEMENT.name,
        ),
        Avatar(
            stringResource(id = R.string.app_name),
            R.drawable.pretty_girl,
            MaterialTheme.colorScheme.primary,
            Routes.CHAT.name,
        ),
        Avatar(
            stringResource(R.string.goal_girl_name),
            R.drawable.goal_girl,
            MaterialColor.Orange900,
            Routes.GOAL.name,
        ),
    )

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPreview() {
    AliciaTheme {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}
