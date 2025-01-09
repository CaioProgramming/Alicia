package com.ilustris.alicia.features.messages.di

import com.ilustris.alicia.features.messages.data.datasource.MessageDao
import com.ilustris.alicia.features.messages.data.datasource.MessageDaoImpl
import com.ilustris.alicia.features.messages.data.repository.MessageRepositoryImpl
import com.ilustris.alicia.features.messages.domain.repository.MessageRepository
import com.ilustris.alicia.features.messages.domain.usecase.ChatUseCase
import com.ilustris.alicia.features.messages.domain.usecase.ChatUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MessagesModule {
    @Binds
    abstract fun bindMessageDao(messageDao: MessageDaoImpl): MessageDao

    @Binds
    abstract fun bindMessageRepository(messageRepository: MessageRepositoryImpl): MessageRepository

    @Binds
    abstract fun bindMessageUseCase(messagesUseCaseImpl: ChatUseCaseImpl): ChatUseCase
}
