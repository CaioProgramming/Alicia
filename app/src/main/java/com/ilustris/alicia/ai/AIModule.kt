package com.ilustris.alicia.ai

import com.ilustris.alicia.ai.callback.CallbackExecutor
import com.ilustris.alicia.ai.callback.CallbackExecutorImpl
import com.ilustris.alicia.ai.inputs.InputGenerator
import com.ilustris.alicia.ai.inputs.InputGeneratorImpl
import com.ilustris.alicia.ai.message.MessageGenerator
import com.ilustris.alicia.ai.message.MessageGeneratorImpl
import com.ilustris.alicia.ai.usecase.AIUseCase
import com.ilustris.alicia.ai.usecase.AIUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AIModule {
    @Binds
    abstract fun bindAIUseCase(useCase: AIUseCaseImpl): AIUseCase

    @Binds
    abstract fun bindCallbackExecutor(callbackExecutorImpl: CallbackExecutorImpl): CallbackExecutor

    @Binds
    abstract fun bindMessageGenerator(messageGeneratorImpl: MessageGeneratorImpl): MessageGenerator

    @Binds
    abstract fun bindInputGenerator(inputGeneratorImpl: InputGeneratorImpl): InputGenerator
}
