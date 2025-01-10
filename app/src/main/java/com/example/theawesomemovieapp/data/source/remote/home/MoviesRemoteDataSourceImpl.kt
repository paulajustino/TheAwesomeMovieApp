package com.example.theawesomemovieapp.data.source.remote.home

import com.example.theawesomemovieapp.BuildConfig

import com.example.theawesomemovieapp.data.source.remote.api.TMDBApiService
import com.example.theawesomemovieapp.utils.Result
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val apiService: TMDBApiService
) : MoviesRemoteDataSource {

    override suspend fun fetchLatestMovieList(): Result<MovieListResponse, String> {
        return try {
            val moviesResponse =
                apiService.getLatestMovieList(apiKey = BuildConfig.TMDB_API_KEY)

            if (moviesResponse.isSuccessful && moviesResponse.body() != null) {
                Result.Success(moviesResponse.body()!!)
            } else {
                val errorMessage = moviesResponse.errorBody()?.string() ?: "Unknown error"
                Result.Error("Movies not found: $errorMessage")
            }
        } catch (e: Exception) {
            Result.Error("Failed to fetch movies: ${e.message}")
        }
    }
}