package com.ilustris.alicia.features.finnance.di

import com.ilustris.alicia.features.finnance.data.datasource.GoalDao
import com.ilustris.alicia.features.finnance.data.datasource.GoalDaoImpl
import com.ilustris.alicia.features.finnance.data.datasource.MovimentationDao
import com.ilustris.alicia.features.finnance.data.datasource.MovimentationDaoImpl
import com.ilustris.alicia.features.finnance.data.repository.FinnanceRepositoryImpl
import com.ilustris.alicia.features.finnance.domain.repository.FinnanceRepository
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class FinnanceModule {

    @Binds
    abstract fun bindsMovimentationDao(movimentationDao: MovimentationDaoImpl): MovimentationDao

    @Binds
    abstract fun goalDao(goalDao: GoalDaoImpl): GoalDao


    @Binds
    abstract fun bindsFinnanceRepository(finnanceRepositoryImpl: FinnanceRepositoryImpl): FinnanceRepository


    @Binds
    abstract fun bindsFinnanceUseCase(finnanceUseCaseImpl: FinanceUseCaseImpl): FinanceUseCase


}