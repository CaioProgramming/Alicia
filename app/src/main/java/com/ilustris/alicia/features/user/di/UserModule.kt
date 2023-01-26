package com.ilustris.alicia.features.user.di


import com.ilustris.alicia.features.user.data.datasource.UserDao
import com.ilustris.alicia.features.user.data.datasource.UserDaoImpl
import com.ilustris.alicia.features.user.data.repository.UserRepositoryImpl
import com.ilustris.alicia.features.user.domain.repository.UserRepository
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import com.ilustris.alicia.features.user.domain.usecase.UserUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserModule {

    @Binds
    abstract fun bindUserDao(userDaoImpl: UserDaoImpl): UserDao

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindUserUseCase(useCaseImpl: UserUseCaseImpl): UserUseCase


}