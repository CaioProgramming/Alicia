package com.ilustris.alicia.features.finnance.presentation

import androidx.lifecycle.ViewModel
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.utils.PreferencesService
import com.ilustris.alicia.utils.STATEMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatementViewModel @Inject constructor(
    financeUseCase: FinanceUseCase,
    private val preferencesService: PreferencesService
) :
    ViewModel() {

    val movimentations = financeUseCase.getMovimentationsByDay()
    val movimentationsByTag = financeUseCase.getAllMovimentations()
    val movimentationLineChart = financeUseCase.getMovimentationsChart()
    val movimentationCircleChart = financeUseCase.getMovimentationsCircleChart()
    val amount = financeUseCase.getAmount()
    fun statementIntro() = preferencesService.getBooleanKey(STATEMENT_KEY)

    fun updateStatementKey() {
        preferencesService.updateBooleanKey(STATEMENT_KEY, true)
    }

}