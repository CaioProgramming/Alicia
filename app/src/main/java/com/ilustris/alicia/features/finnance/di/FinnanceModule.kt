package com.ilustris.alicia.features.finnance.di

import com.ilustris.alicia.features.finnance.data.datasource.MovimentationDao
import com.ilustris.alicia.features.finnance.data.datasource.MovimentationDaoImpl
import com.ilustris.alicia.features.finnance.data.repository.FinnanceRepositoryImpl
import com.ilustris.alicia.features.finnance.domain.repository.FinnanceRepository
import com.ilustris.alicia.features.finnance.domain.usecase.FinnanceUseCase
import com.ilustris.alicia.features.finnance.domain.usecase.FinnanceUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class FinnanceModule {

    @Binds
    abstract fun bindsMovimentationDao(movimentationDao: MovimentationDaoImpl) : MovimentationDao

    @Binds
    abstract fun bindsFinnanceRepository(finnanceRepositoryImpl: FinnanceRepositoryImpl) : FinnanceRepository


    @Binds
    abstract fun bindsFinnanceUseCase(finnanceUseCaseImpl: FinnanceUseCaseImpl) : FinnanceUseCase


}