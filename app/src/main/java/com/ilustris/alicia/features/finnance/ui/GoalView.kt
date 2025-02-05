package com.ilustris.alicia.features.finnance.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ilustris.alicia.R
import com.ilustris.alicia.core.theme.AliciaTheme
import com.ilustris.alicia.core.theme.toolbarColor
import com.ilustris.alicia.features.finnance.presentation.GoalViewModel
import com.ilustris.alicia.features.finnance.ui.component.GoalMedalV2
import com.ilustris.alicia.features.finnance.ui.component.GoalMedalV3
import com.ilustris.alicia.features.finnance.ui.component.SheetInfo
import com.ilustris.alicia.features.home.ui.getAvatars
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GoalScreen(navController: NavController) {
    val goalViewModel: GoalViewModel = hiltViewModel()
    val goals = goalViewModel.goals.collectAsState(initial = emptyList())
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val scope = rememberCoroutineScope()

    AliciaTheme {
        ModalBottomSheetLayout(
            sheetState = bottomSheetState,
            sheetShape = RoundedCornerShape(15.dp),
            sheetContent = {
                val avatar = getAvatars().last()
                SheetInfo(
                    title = "Conheça a ${avatar.name}",
                    description = stringResource(R.string.goal_tutorial, avatar.name),
                    avatar = avatar,
                ) {
                    scope.launch {
                        goalViewModel.updateGoalKey()
                        bottomSheetState.hide()
                    }
                }
            },
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(toolbarColor(isSystemInDarkTheme())),
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                ) {
                    goals.value.forEach {
                        item(span = { GridItemSpan(2) }) {
                            Column {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(
                                                MaterialTheme.colorScheme.onBackground.copy(
                                                    alpha = 0.3f,
                                                ),
                                            ),
                                )
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = it.header,
                                    style =
                                        MaterialTheme.typography.headlineSmall.copy(
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontWeight = FontWeight.W800,
                                        ),
                                )
                                Text(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    text = it.description,
                                    style =
                                        MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontWeight = FontWeight.W300,
                                        ),
                                )
                            }
                        }
                        items(it.goals.size) { index ->
                            GoalMedalV3(goal = it.goals[index], true, true, Modifier.size(200.dp))
                        }
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            scope.launch {
                if (!goalViewModel.goalIntro()) {
                    bottomSheetState.show()
                } else {
                    bottomSheetState.hide()
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
