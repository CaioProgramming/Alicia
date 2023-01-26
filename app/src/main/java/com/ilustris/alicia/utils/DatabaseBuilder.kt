package com.ilustris.alicia.utils

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DatabaseBuilder @Inject constructor(@ApplicationContext private val context: Context) {

    fun buildDataBase() = Room.databaseBuilder(
        context,
        AliciaDatabase::class.java,
        AliciaDatabase::class.java.simpleName
    ).fallbackToDestructiveMigration().build()
}