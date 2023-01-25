package com.ilustris.alicia.features.finnance.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimentationDao {

    @Insert
    suspend fun saveMovimentation(movimentation: Movimentation) : Long

    @Query("Select * from Movimentation")
    fun getMovimentations() : Flow<List<Movimentation>>

}