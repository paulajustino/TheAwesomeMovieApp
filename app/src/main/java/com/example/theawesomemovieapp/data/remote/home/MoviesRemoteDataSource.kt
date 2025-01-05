package com.example.theawesomemovieapp.data.remote.home

import com.example.theawesomemovieapp.ui.model.Movie
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result

interface MoviesRemoteDataSource {
    suspend fun fetchLatestMovieList(): Result<List<Movie>, NetworkError>
}