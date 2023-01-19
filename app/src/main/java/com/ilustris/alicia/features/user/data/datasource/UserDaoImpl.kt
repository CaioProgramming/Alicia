package com.ilustris.alicia.features.user.data.datasource

import com.ilustris.alicia.features.user.data.User
import com.ilustris.alicia.utils.DatabaseBuilder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDaoImpl @Inject constructor(private val databaseBuilder: DatabaseBuilder): UserDao {

    private val database by lazy {  databaseBuilder.buildDataBase() }

    override suspend fun saveUser(user: User) : Long {
      return database.userDao().saveUser(user)
    }

    override suspend fun delete(user: User) {
        database.userDao().delete(user)
    }

    override suspend fun updateUser(user: User) {
       database.userDao().updateUser(user)
    }

    override fun getUserById(uid: Long): Flow<User?> = database.userDao().getUserById(uid)


}