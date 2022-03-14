package com.pelsoczi.edamam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton


/** Network components configured to use throughout the application */
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, SECONDS)
            .readTimeout(10, SECONDS)
            .writeTimeout(10, SECONDS)
            .build()
    }

}