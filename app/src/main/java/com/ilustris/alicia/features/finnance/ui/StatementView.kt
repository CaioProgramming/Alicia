package com.ilustris.alicia.features.finnance.ui

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.himanshoe.charty.circle.CircleChart
import com.himanshoe.charty.common.dimens.ChartDimens
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.LineConfig
import com.ilustris.alicia.core.theme.AliciaTheme
import com.ilustris.alicia.core.theme.toolbarColor
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.finnance.presentation.SheetType
import com.ilustris.alicia.features.finnance.presentation.StatementViewModel
import com.ilustris.alicia.features.finnance.ui.component.AmountComponent
import com.ilustris.alicia.features.finnance.ui.component.MovimentationSheet
import com.ilustris.alicia.features.finnance.ui.component.SheetInfo
import com.ilustris.alicia.features.home.ui.getAvatars
import com.ilustris.alicia.features.messages.ui.components.StatementComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatementScreen(navController: NavController) {
    val viewModel: StatementViewModel = hiltViewModel()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val movimentationsByDate by viewModel.movimentations.collectAsState(initial = emptyList())
    val movimentationsByTag by viewModel.movimentationsByTag.collectAsState(initial = emptyList())
    val movimentationList =
        if (pagerState.currentPage == 0) movimentationsByDate else movimentationsByTag
    val movimentationsChart by viewModel.movimentationLineChart.collectAsState(initial = emptyList())
    val movimentationsCircleChart by
        viewModel.movimentationCircleChart.collectAsState(initial = emptyList())
    val statementIntro = viewModel.statementIntro()
    val avatar = getAvatars().first()
    val sheetType =
        remember {
            mutableStateOf(SheetType.TUTORIAL)
        }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            AnimatedContent(sheetType.value) {
                when (it) {
                    SheetType.TUTORIAL -> {
                        SheetInfo(
                            title = stringResource(it.title, avatar.name),
                            description = stringResource(it.description, avatar.name),
                            avatar = avatar,
                        ) {
                            viewModel.updateStatementKey()
                            scope.launch {
                                bottomSheetState.hide()
                            }
                        }
                    }
                    else ->
                        MovimentationSheet(
                            stringResource(it.title),
                            stringResource(it.description),
                        ) {
                            scope.launch {
                                bottomSheetState.hide()
                            }
                            viewModel.saveMovimentation(it, sheetType.value)
                        }
                }
            }
        },
    ) {
        LazyColumn {
            item {
                val currentAmount =
                    movimentationsByDate
                        .filter { it.movimentations.isNotEmpty() }
                        .sumOf { it.movimentations.sumOf { m -> m.value } }

                Log.d("StatementView", "StatementScreen: $currentAmount")
                AmountComponent(amount = currentAmount)
            }

            if (movimentationList.isEmpty()) {
                item {
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        text = "Você ainda não possui movimentações",
                        textAlign = TextAlign.Center,
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    val buttonModifier =
                        Modifier
                            .padding(12.dp)
                            .background(MaterialTheme.colorScheme.background, CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                            .padding(16.dp)
                            .size(12.dp)

                    val iconColor = MaterialTheme.colorScheme.onBackground

                    IconButton(
                        modifier = buttonModifier,
                        onClick = {
                            scope.launch {
                                sheetType.value = SheetType.INCOME
                                bottomSheetState.show()
                            }
                        },
                    ) {
                        Icon(
                            Icons.Rounded.KeyboardArrowUp,
                            contentDescription = "Add Movimentation",
                            tint = iconColor,
                        )
                    }

                    IconButton(
                        modifier = buttonModifier,
                        onClick = {
                            scope.launch {
                                sheetType.value = SheetType.EXPENSE
                                bottomSheetState.show()
                            }
                        },
                    ) {
                        Icon(
                            Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "Add Expense",
                            tint = iconColor,
                        )
                    }
                }
            }

            item {
                HorizontalPager(
                    state = pagerState,
                    modifier =
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(300.dp),
                ) {
                    when (it) {
                        0 ->
                            if (movimentationsChart.isNotEmpty()) {
                                LineChart(
                                    lineData = movimentationsChart,
                                    lineConfig =
                                        LineConfig(
                                            hasSmoothCurve = true,
                                            hasDotMarker = true,
                                        ),
                                    chartDimens = ChartDimens(8.dp),
                                    modifier =
                                        Modifier
                                            .padding(8.dp)
                                            .fillMaxSize(),
                                    colors =
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary,
                                            MaterialTheme.colorScheme.tertiary,
                                        ),
                                )
                            }

                        1 ->
                            if (movimentationsCircleChart.isNotEmpty()) {
                                CircleChart(
                                    circleData = movimentationsCircleChart,
                                    isAnimated = true,
                                    modifier =
                                        Modifier
                                            .padding(8.dp)
                                            .fillMaxSize(),
                                )
                            }
                    }
                }
            }

            movimentationList.forEach {
                stickyHeader {
                    Text(
                        text = it.header,
                        style =
                            MaterialTheme.typography.labelSmall.copy(
                                color =
                                    MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.W600,
                            ),
                        modifier =
                            Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .fillMaxWidth()
                                .padding(16.dp),
                    )
                }
                items(it.movimentations) {
                    StatementComponent(
                        movimentation = it,
                        textColor = MaterialTheme.colorScheme.onBackground,
                        onRemove = {
                            viewModel.removeMovimentation(it)
                        },
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            if (!statementIntro) {
                bottomSheetState.show()
            } else {
                bottomSheetState.hide()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatementList(
    movimentations: List<MovimentationInfo>,
    onRemove: ((Movimentation) -> Unit)? = null,
    onSelect: (Movimentation) -> Unit = {},
) {
    LazyColumn(modifier = Modifier.background(toolbarColor(isSystemInDarkTheme()))) {
        movimentations.forEach {
            stickyHeader {
                Text(
                    text = it.header,
                    style =
                        MaterialTheme.typography.labelSmall.copy(
                            color =
                                MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.5f,
                                ),
                            fontWeight = FontWeight.W600,
                        ),
                    modifier =
                        Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .fillMaxWidth()
                            .padding(16.dp),
                )
            }
            items(it.movimentations.size) { index ->
                val movimentation = it.movimentations[index]
                StatementComponent(
                    movimentation = movimentation,
                    textColor = MaterialTheme.colorScheme.onBackground,
                    onRemove =
                        onRemove?.let {
                            {
                                onRemove(movimentation)
                            }
                        },
                    onClick = {
                        onSelect(movimentation)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun StatementPreview() {
    AliciaTheme {
        val navController = rememberNavController()
        StatementScreen(navController)
    }
}
