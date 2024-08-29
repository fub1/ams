// NetworkModule.kt
package com.crty.ams.core.data.network.di

import com.crty.ams.core.data.network.api.CoreApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideCoreApiService(): CoreApiService {
        return CoreApiService.create()
    }
}