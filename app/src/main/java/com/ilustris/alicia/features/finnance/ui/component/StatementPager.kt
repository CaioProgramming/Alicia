package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatementPager(movimentationsInfo: List<MovimentationInfo>, tagIndex: Int?) {
    val pagerState = rememberPagerState()
    HorizontalPager(pageCount = movimentationsInfo.size, state = pagerState) {
        StatementPage(movimentationInfo = movimentationsInfo[it])
    }
    LaunchedEffect(pagerState) {
        tagIndex?.let {
            pagerState.scrollToPage(tagIndex)
        }
    }
}