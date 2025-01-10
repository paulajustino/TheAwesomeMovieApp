package com.example.theawesomemovieapp.di

import android.content.Context
import androidx.room.Room
import com.example.theawesomemovieapp.data.source.local.dao.MovieDao
import com.example.theawesomemovieapp.data.source.local.dao.MovieImagesDao
import com.example.theawesomemovieapp.data.source.local.database.AppDataBase
import com.example.theawesomemovieapp.data.source.local.database.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java,
            "app_movie_data_base"
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(dataBase: AppDataBase): MovieDao = dataBase.movieDao()

    @Provides
    @Singleton
    fun provideMovieImagesDao(dataBase: AppDataBase): MovieImagesDao = dataBase.movieImagesDao()
}