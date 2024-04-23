package com.ilustris.alicia.ai

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

}