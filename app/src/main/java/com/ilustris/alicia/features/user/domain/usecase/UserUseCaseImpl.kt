package com.ilustris.alicia.features.user.domain.usecase

import com.ilustris.alicia.features.user.data.User
import com.ilustris.alicia.features.user.domain.repository.UserRepository
import com.ilustris.alicia.utils.PreferencesService
import com.ilustris.alicia.utils.USER_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesService: PreferencesService
) : UserUseCase {

    override suspend fun updateUser(user: User) {
        userRepository.updateUser(user)
    }

    override suspend fun saveUser(username: String, avatar: Int) = flow<Long> {
        val newUser = userRepository.saveUser(username, avatar = avatar)
        preferencesService.updateLongKey(USER_KEY, newUser)
        emit(newUser)
    }

    override suspend fun getUserById(): Flow<User?> = userRepository.getUserByUid(preferencesService.getLongKey(USER_KEY))


}