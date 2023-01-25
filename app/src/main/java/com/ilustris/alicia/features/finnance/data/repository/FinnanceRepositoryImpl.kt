package com.ilustris.alicia.features.finnance.data.repository

import com.ilustris.alicia.features.finnance.data.datasource.MovimentationDao
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.domain.repository.FinnanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FinnanceRepositoryImpl @Inject constructor(private val movimentationDao: MovimentationDao): FinnanceRepository {

    override suspend fun saveMovimentation(movimentation: Movimentation): Long = movimentationDao.saveMovimentation(movimentation)

    override fun getMovimentations(): Flow<List<Movimentation>> = movimentationDao.getMovimentations()

}