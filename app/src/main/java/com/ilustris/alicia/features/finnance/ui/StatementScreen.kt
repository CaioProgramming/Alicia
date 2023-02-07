
package com.ilustris.alicia.features.finnance.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ilustris.alicia.features.finnance.presentation.StatementViewModel
import com.ilustris.alicia.features.messages.ui.components.StatementComponent
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatementScreen(navController: NavController) {
    val viewModel: StatementViewModel = hiltViewModel()
    val movimentations = viewModel.movimentations.collectAsState(initial = emptyList())
    AliciaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(toolbarColor(isSystemInDarkTheme()))
        ) {
            Text(
                text = "Histórico de movimentações",
                style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn {
                movimentations.value.forEach {
                    stickyHeader {
                        Text(
                            text = it.tag.description,
                            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    items(it.movimentations.size) { index ->
                        StatementComponent(
                            movimentation = it.movimentations[index],
                            modifier = Modifier.fillMaxWidth(),
                            clipText = false,
                            textColor = MaterialTheme.colorScheme.onBackground,
                            showTag = true
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