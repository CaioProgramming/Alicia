package com.ilustris.alicia.features.user.data.repository

import android.util.Log
import com.ilustris.alicia.features.user.data.datasource.UserDao
import com.ilustris.alicia.features.user.data.model.User
import com.ilustris.alicia.features.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    override suspend fun saveUser(userName: String, avatar: Int): Long {
        return userDao.saveUser(User(name = userName, avatar = avatar))
    }

    override fun getUserByUid(uid: Long): Flow<User?> {
        Log.i(javaClass.simpleName, "query user with id = $uid")
        return userDao.getUserById(uid)
    }

}