package com.ilustris.alicia.features.finnance.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.himanshoe.charty.circle.CircleChart
import com.himanshoe.charty.common.dimens.ChartDimens
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.LineConfig
import com.ilustris.alicia.R
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.finnance.presentation.StatementViewModel
import com.ilustris.alicia.features.messages.ui.components.AmountComponent
import com.ilustris.alicia.features.messages.ui.components.StatementComponent
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatementScreen(navController: NavController) {
    val viewModel: StatementViewModel = hiltViewModel()
    val pagerState = rememberPagerState(initialPage = 0)
    val movimentationsByDate = viewModel.movimentations.collectAsState(initial = emptyList())
    val movimentationsByTag = viewModel.movimentationsByTag.collectAsState(initial = emptyList())
    val movimentationList =
        if (pagerState.currentPage == 0) movimentationsByDate.value else movimentationsByTag.value
    val movimentationsChart = viewModel.movimentationLineChart.collectAsState(initial = emptyList())
    val movimentationsCircleChart =
        viewModel.movimentationCircleChart.collectAsState(initial = emptyList())
    val currentAmount = viewModel.amount.collectAsState(initial = 0.0)


    AliciaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = toolbarColor(isSystemInDarkTheme()))
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
                    text = "Movimentações",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.W900
                    ),
                )
                if (currentAmount.value > 0) {
                    AmountComponent(amount = currentAmount.value)
                }

                HorizontalPager(
                    pageCount = 2,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(275.dp)
                ) {
                    when (it) {
                        0 -> if (movimentationsChart.value.isNotEmpty()) {
                            LineChart(
                                lineData = movimentationsChart.value,
                                lineConfig = LineConfig(hasSmoothCurve = true, hasDotMarker = true),
                                chartDimens = ChartDimens(8.dp),
                                modifier = Modifier
                                    .fillMaxSize(0.9f)
                                    .padding(8.dp),
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary
                                ),
                            )
                        }
                        1 -> if (movimentationsCircleChart.value.isNotEmpty()) {
                            CircleChart(
                                circleData = movimentationsCircleChart.value,
                                isAnimated = true,
                                modifier = Modifier
                                    .fillMaxSize(0.9f)
                                    .padding(8.dp)
                            )
                        }
                    }
                }

            }
            if (movimentationList.isNotEmpty()) {
                StatementList(movimentations = movimentationList)
            } else {
                Text(
                    text = "Você não possui movimentações.",
                    style = MaterialTheme.typography.displayMedium.copy(color = MaterialTheme.colorScheme.onBackground)
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatementList(movimentations: List<MovimentationInfo>) {
    LazyColumn(modifier = Modifier.background(toolbarColor(isSystemInDarkTheme()))) {
        movimentations.forEach {
            stickyHeader {
                Text(
                    text = it.header,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.5f
                        ), fontWeight = FontWeight.W600
                    ),
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            items(it.movimentations.size) { index ->
                StatementComponent(
                    movimentation = it.movimentations[index],
                    textColor = MaterialTheme.colorScheme.onBackground,
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