package com.example.theawesomemovieapp.model.repository.home

import com.example.theawesomemovieapp.model.data.Movie
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result

interface MoviesRepository {

    suspend fun getLatestMovieList(): Result<List<Movie>, NetworkError>
}