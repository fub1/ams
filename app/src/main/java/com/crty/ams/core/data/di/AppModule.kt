package com.crty.ams.core.data.di

import android.content.Context
import com.crty.ams.core.data.datastore.di.AppParameterDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideAppParameterDataStore(@ApplicationContext context: Context): AppParameterDataStore {
        return AppParameterDataStore(context)
    }

}


