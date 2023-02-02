package com.ilustris.alicia

import android.content.Context
import com.ilustris.alicia.features.finnance.domain.mapper.MovimentationMapper
import com.ilustris.alicia.features.messages.domain.mapper.MessageMapper
import com.ilustris.alicia.utils.AliciaDatabase
import com.ilustris.alicia.utils.DatabaseBuilder
import com.ilustris.alicia.utils.PreferencesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): AliciaDatabase {
        return DatabaseBuilder(context).buildDataBase()
    }

    @Singleton
    @Provides
    fun preferencesService(@ApplicationContext context: Context): PreferencesService {
        return PreferencesService(context)
    }

    @Singleton
    @Provides
    fun providesMessageMapper(): MessageMapper {
        return MessageMapper()
    }

    @Singleton
    @Provides
    fun providesMovimentationMapper(): MovimentationMapper {
        return MovimentationMapper()
    }
}