package com.example.theawesomemovieapp.data.source.local.home

import com.example.theawesomemovieapp.domain.model.Movie

interface MoviesLocalDataSource {
    suspend fun getLocalMoviesData(): List<Movie>?
    suspend fun saveOrUpdateMovieData(movie: Movie)
    suspend fun clearOldMoviesData(moviesIds: List<Int>)
}