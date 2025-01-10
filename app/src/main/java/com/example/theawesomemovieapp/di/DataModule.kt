package com.example.theawesomemovieapp.di

import com.example.theawesomemovieapp.data.preferences.CachePreferences
import com.example.theawesomemovieapp.data.preferences.CachePreferencesImpl
import com.example.theawesomemovieapp.data.source.local.details.MovieDetailsLocalDataSource
import com.example.theawesomemovieapp.data.source.local.details.MovieDetailsLocalDataSourceImpl
import com.example.theawesomemovieapp.data.source.local.home.MoviesLocalDataSource
import com.example.theawesomemovieapp.data.source.local.home.MoviesLocalDataSourceImpl
import com.example.theawesomemovieapp.data.source.remote.details.MovieDetailsRemoteDataSource
import com.example.theawesomemovieapp.data.source.remote.details.MovieDetailsRemoteDataSourceImpl
import com.example.theawesomemovieapp.data.source.remote.home.MoviesRemoteDataSource
import com.example.theawesomemovieapp.data.source.remote.home.MoviesRemoteDataSourceImpl
import com.example.theawesomemovieapp.data.repository.details.MovieDetailsRepository
import com.example.theawesomemovieapp.data.repository.details.MovieDetailsRepositoryImpl
import com.example.theawesomemovieapp.data.repository.home.MoviesRepository
import com.example.theawesomemovieapp.data.repository.home.MoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindMovieDetailsLocalDataSource(
        movieDetailsLocalDataSource: MovieDetailsLocalDataSourceImpl
    ): MovieDetailsLocalDataSource

    @Binds
    @Singleton
    abstract fun bindMoviesLocalDataSource(
        moviesLocalDataSource: MoviesLocalDataSourceImpl
    ): MoviesLocalDataSource

    @Binds
    @Singleton
    abstract fun bindMovieDetailsRemoteDataSource(
        movieDetailsRemoteDataSource: MovieDetailsRemoteDataSourceImpl
    ): MovieDetailsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMoviesRemoteDataSource(
        moviesRemoteDataSource: MoviesRemoteDataSourceImpl
    ): MoviesRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMovieDetailsRepository(
        movieDetailsRepository: MovieDetailsRepositoryImpl
    ): MovieDetailsRepository

    @Binds
    @Singleton
    abstract fun bindMoviesRepository(
        moviesRepository: MoviesRepositoryImpl
    ): MoviesRepository

    @Binds
    abstract fun bindCachePreferences(
        cachePreferences: CachePreferencesImpl
    ): CachePreferences
}