package com.ilustris.alicia.features.finnance.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ilustris.alicia.R
import com.ilustris.alicia.features.finnance.presentation.GoalViewModel
import com.ilustris.alicia.features.finnance.ui.component.GoalMedal
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GoalScreen(navController: NavController) {
    val goalViewModel: GoalViewModel = hiltViewModel()
    val goals = goalViewModel.goals.collectAsState(initial = emptyList())

    AliciaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(toolbarColor(isSystemInDarkTheme()))
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.round_chevron_left_24),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    contentDescription = "Voltar",
                )
            }
            Text(
                text = "Metas",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Black
                )
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                goals.value.forEach {
                    item(span = { GridItemSpan(3) }) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
                            )
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = it.header,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.W800
                                )
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = it.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.W300
                                )
                            )
                        }

                    }
                    items(it.goals.size) { index ->
                        GoalMedal(goal = it.goals[index], 100.dp, true) {}
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalPreview() {
    AliciaTheme {
        val navController = rememberNavController()
        GoalScreen(navController)
    }
}