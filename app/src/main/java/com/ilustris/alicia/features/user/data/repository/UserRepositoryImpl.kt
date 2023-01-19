package com.ilustris.alicia.features.user.data.repository

import androidx.room.Room
import com.ilustris.alicia.features.user.data.User
import com.ilustris.alicia.features.user.data.datasource.UserDao
import com.ilustris.alicia.features.user.domain.repository.UserRepository
import com.ilustris.alicia.utils.PreferencesService
import com.ilustris.alicia.utils.USER_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao, private val  preferencesService: PreferencesService) : UserRepository {

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    override suspend fun saveUser(userName: String, avatar: Int) {
        val newUser = userDao.saveUser(User(name = userName, avatar = avatar))
        preferencesService.updateLongKey(USER_KEY, newUser)
    }

    override suspend fun getUserByUid(uid: Int): Flow<User> = flow<User> {
        val user = userDao.getUserById(uid)
        emit(user)
    }

}