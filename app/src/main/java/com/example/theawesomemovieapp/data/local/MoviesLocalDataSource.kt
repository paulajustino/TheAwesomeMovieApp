package com.example.theawesomemovieapp.data.local

import com.example.theawesomemovieapp.ui.model.Movie

interface MoviesLocalDataSource {
    suspend fun getLocalMoviesData(): List<Movie>?
    suspend fun saveMoviesData(movieList: List<Movie>)
    suspend fun clearMoviesData()
}