package com.ilustris.alicia.features.finnance.data.datasource

import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.utils.DatabaseBuilder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovimentationDaoImpl @Inject constructor (private val databaseBuilder: DatabaseBuilder): MovimentationDao {

    private val database by lazy {  databaseBuilder.buildDataBase() }

    override suspend fun saveMovimentation(movimentation: Movimentation): Long = database.movimentationDao().saveMovimentation(movimentation)


    override fun getMovimentations(): Flow<List<Movimentation>> = database.movimentationDao().getMovimentations()

}