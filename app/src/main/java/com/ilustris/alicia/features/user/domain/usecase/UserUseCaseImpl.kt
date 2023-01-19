package com.ilustris.alicia.features.user.domain.usecase

import com.ilustris.alicia.features.user.data.User
import com.ilustris.alicia.features.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(private val userRepository: UserRepository): UserUseCase {

    override suspend fun updateUser(user: User) {
        userRepository.updateUser(user)
    }

    override suspend fun saveUser(username: String, avatar: Int) {
        userRepository.saveUser(username, avatar = avatar)
    }

    override suspend fun getUserById(uid: Int): Flow<User> = userRepository.getUserByUid(uid)

}