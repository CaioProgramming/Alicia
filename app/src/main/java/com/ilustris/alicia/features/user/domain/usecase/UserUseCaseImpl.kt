package com.ilustris.alicia.features.user.domain.usecase

import com.ilustris.alicia.features.user.data.model.User
import com.ilustris.alicia.features.user.domain.repository.UserRepository
import com.ilustris.alicia.utils.PreferencesService
import com.ilustris.alicia.utils.USER_KEY
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesService: PreferencesService
) : UserUseCase {

    override suspend fun updateUser(user: User) {
        userRepository.updateUser(user)
    }

    override suspend fun saveUser(username: String, avatar: Int) : Long  {
        val newUser = userRepository.saveUser(username, avatar = avatar)
        preferencesService.updateLongKey(USER_KEY, newUser)
        return newUser
    }

    override fun getUserById() =
        userRepository.getUserByUid(preferencesService.getLongKey(USER_KEY))


}