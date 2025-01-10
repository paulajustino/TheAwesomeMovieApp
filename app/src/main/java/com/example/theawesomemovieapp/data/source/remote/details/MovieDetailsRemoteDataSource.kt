package com.example.theawesomemovieapp.data.source.remote.details

import com.example.theawesomemovieapp.data.source.remote.home.MovieResponse
import com.example.theawesomemovieapp.utils.Result

interface MovieDetailsRemoteDataSource {
    suspend fun fetchMovieDetails(movieId: String): Result<MovieResponse, String>
    suspend fun fetchMovieImages(movieId: String): Result<MovieImageListResponse, String>
}