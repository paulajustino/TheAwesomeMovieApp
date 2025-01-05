package com.example.theawesomemovieapp.data.repository.home

import com.example.theawesomemovieapp.ui.model.Movie
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result

interface MoviesRepository {

    suspend fun getLatestMovieList(): Result<List<Movie>, NetworkError>
}