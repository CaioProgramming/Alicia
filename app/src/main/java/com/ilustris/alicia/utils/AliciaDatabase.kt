package com.ilustris.alicia.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.messages.data.datasource.MessageDao
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.user.data.model.User
import com.ilustris.alicia.features.user.data.datasource.UserDao

@Database(entities = [User::class, Goal::class, Message::class, Movimentation::class], version = 1)
abstract class AliciaDatabase: RoomDatabase() {

    abstract fun userDao() : UserDao
    abstract fun messageDao(): MessageDao

}