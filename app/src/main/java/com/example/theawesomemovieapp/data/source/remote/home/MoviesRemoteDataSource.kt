package com.example.theawesomemovieapp.data.source.remote.home

import com.example.theawesomemovieapp.utils.Result

interface MoviesRemoteDataSource {
    suspend fun fetchLatestMovieList(): Result<MovieListResponse, String>
}