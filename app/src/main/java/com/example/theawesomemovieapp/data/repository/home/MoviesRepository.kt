package com.example.theawesomemovieapp.data.repository.home

import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.utils.Result

interface MoviesRepository {
    suspend fun getLatestMovieList(): Result<List<Movie>, String>
}