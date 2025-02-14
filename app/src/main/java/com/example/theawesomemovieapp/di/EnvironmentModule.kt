package com.example.theawesomemovieapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object EnvironmentModule {

    @Provides
    @TestEnvironment
    fun provideIsTestEnvironment(): Boolean = false
}