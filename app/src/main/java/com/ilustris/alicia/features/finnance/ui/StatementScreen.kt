package com.ilustris.alicia.features.finnance.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ilustris.alicia.features.finnance.presentation.StatementViewModel
import com.ilustris.alicia.features.finnance.ui.component.StatementPager
import com.ilustris.alicia.ui.theme.AliciaTheme

@Composable
fun StatementScreen(navController: NavController) {
    val viewModel: StatementViewModel = hiltViewModel()
    val movimentations = viewModel.movimentations.collectAsState(initial = emptyList())
    val tagIndex = navController.previousBackStackEntry?.arguments?.getInt("tag")
    StatementPager(movimentationsInfo = movimentations.value, tagIndex)
}

@Preview
@Composable
fun StatementPreview() {
    AliciaTheme {
        val navController = rememberNavController()
        StatementScreen(navController = navController)
    }

}