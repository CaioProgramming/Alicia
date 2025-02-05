package com.ilustris.alicia.features.finnance.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilustris.alicia.R
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.utils.PreferencesService
import com.ilustris.alicia.utils.STATEMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatementViewModel
    @Inject
    constructor(
        private val financeUseCase: FinanceUseCase,
        private val preferencesService: PreferencesService,
    ) : ViewModel() {
        val movimentations = financeUseCase.getMovimentationsByDay()
        val movimentationsByTag = financeUseCase.getAllMovimentations()
        val movimentationLineChart = financeUseCase.getMovimentationsChart()
        val movimentationCircleChart = financeUseCase.getMovimentationsCircleChart()

        fun statementIntro() = preferencesService.getBooleanKey(STATEMENT_KEY)

        fun updateStatementKey() {
            preferencesService.updateBooleanKey(STATEMENT_KEY, true)
        }

        fun removeMovimentation(movimentation: Movimentation) {
            viewModelScope.launch(Dispatchers.IO) {
                financeUseCase.deleteMovimentation(movimentation)
            }
        }

        fun saveMovimentation(
            movimentation: Movimentation,
            type: SheetType,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                val newMovimentation =
                    if (type == SheetType.EXPENSE) {
                        movimentation.copy(value = movimentation.value.unaryMinus())
                    } else {
                        movimentation
                    }
                financeUseCase.saveMovimentation(newMovimentation)
                updateStatementKey()
            }
        }
    }

enum class SheetType(
    @StringRes val title: Int,
    @StringRes val description: Int,
) {
    INCOME(R.string.statement_income_title, R.string.statement_income_message),
    EXPENSE(R.string.statement_expense_title, R.string.statement_expense_message),
    TUTORIAL(R.string.statement_intro, R.string.statement_intro_message),
}
