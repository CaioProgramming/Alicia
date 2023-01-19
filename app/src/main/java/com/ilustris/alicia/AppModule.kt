package com.ilustris.alicia

import android.content.Context
import com.ilustris.alicia.utils.AliciaDatabase
import com.ilustris.alicia.utils.DatabaseBuilder
import com.ilustris.alicia.utils.PreferencesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Provides
    fun providesDatabase(@ApplicationContext context: Context) : AliciaDatabase { return DatabaseBuilder(context).buildDataBase() }

    @Provides
    fun preferencesService(@ApplicationContext context: Context) : PreferencesService { return PreferencesService(context) }
}