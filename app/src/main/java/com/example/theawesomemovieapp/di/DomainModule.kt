package com.example.theawesomemovieapp.di

import com.example.theawesomemovieapp.domain.usecase.details.GetMovieDetails
import com.example.theawesomemovieapp.domain.usecase.details.GetMovieDetailsUseCase
import com.example.theawesomemovieapp.domain.usecase.details.GetMovieImages
import com.example.theawesomemovieapp.domain.usecase.details.GetMovieImagesUseCase
import com.example.theawesomemovieapp.domain.usecase.home.GetMovies
import com.example.theawesomemovieapp.domain.usecase.home.GetMoviesUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindGetMovieDetailsUseCase(
        getMovieDetails: GetMovieDetails
    ): GetMovieDetailsUseCase

    @Binds
    @Singleton
    abstract fun bindGetMovieImagesUseCase(
        getMovieImages: GetMovieImages
    ): GetMovieImagesUseCase

    @Binds
    @Singleton
    abstract fun bindGetMoviesUseCase(
        getMovies: GetMovies
    ): GetMoviesUseCase
}