package com.ilustris.alicia.features.finnance.presentation

import androidx.lifecycle.ViewModel
import com.ilustris.alicia.features.finnance.domain.usecase.FinnanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatementViewModel @Inject constructor(private val finnanceUseCase: FinnanceUseCase) :
    ViewModel() {

    val movimentations = finnanceUseCase.getAllMovimentations()

}