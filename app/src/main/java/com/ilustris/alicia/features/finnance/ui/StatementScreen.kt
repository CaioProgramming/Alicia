package com.ilustris.alicia.features.finnance.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
import com.ilustris.alicia.features.finnance.presentation.StatementViewModel
import com.ilustris.alicia.features.messages.ui.components.AmountComponent
import com.ilustris.alicia.features.messages.ui.components.StatementComponent
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatementScreen(navController: NavController) {
    val viewModel: StatementViewModel = hiltViewModel()
    val movimentations = viewModel.movimentations.collectAsState(initial = emptyList())
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
                Text(
                    text = "Movimentações",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Black
                    ),
                )
                if (currentAmount.value > 0) {
                    AmountComponent(amount = currentAmount.value)
                }

                HorizontalPager(
                    pageCount = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    when (it) {
                        0 -> if (movimentationsChart.value.isNotEmpty()) {
                            LineChart(
                                lineData = movimentationsChart.value,
                                lineConfig = LineConfig(hasSmoothCurve = true, hasDotMarker = true),
                                chartDimens = ChartDimens(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(350.dp),
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
                                    .fillMaxWidth()
                                    .height(350.dp)
                                    .padding(16.dp)
                            )
                        }
                    }
                }

            }

            LazyColumn(modifier = Modifier.background(toolbarColor(isSystemInDarkTheme()))) {
                movimentations.value.forEach {
                    stickyHeader {
                        val firstItem = it.movimentations.first()
                        val date = Calendar.getInstance().apply {
                            timeInMillis = firstItem.spendAt
                        }
                        Text(
                            text = date.time.format(DateFormats.DD_OF_MM),
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
    }

}

@Preview
@Composable
fun StatementPreview() {
    AliciaTheme {
        val navController = rememberNavController()
        StatementScreen(navController = navController)
    }

}