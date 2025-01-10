package com.example.theawesomemovieapp.data.source.remote.details

import com.example.theawesomemovieapp.BuildConfig
import com.example.theawesomemovieapp.data.source.remote.api.TMDBApiService
import com.example.theawesomemovieapp.data.source.remote.home.MovieResponse
import com.example.theawesomemovieapp.utils.Result

import javax.inject.Inject

class MovieDetailsRemoteDataSourceImpl @Inject constructor(
    private val apiService: TMDBApiService
) : MovieDetailsRemoteDataSource {
    override suspend fun fetchMovieDetails(movieId: String): Result<MovieResponse, String> {
        return try {
            val movieDetailsResponse =
                apiService.getMovieDetails(movieId.toInt(), BuildConfig.TMDB_API_KEY)
            if (movieDetailsResponse.isSuccessful && movieDetailsResponse.body() != null) {
                Result.Success(movieDetailsResponse.body()!!)
            } else {
                val errorMessage = movieDetailsResponse.errorBody()?.string() ?: "Unknown error"
                Result.Error("Movie details not found: $errorMessage")
            }
        } catch (e: Exception) {
            Result.Error("Failed to fetch movie details: ${e.message}")
        }
    }

    override suspend fun fetchMovieImages(movieId: String): Result<MovieImageListResponse, String> {
        return try {
            val movieImagesResponse =
                apiService.getMovieImages(movieId.toInt(), BuildConfig.TMDB_API_KEY)
            if (movieImagesResponse.isSuccessful && movieImagesResponse.body() != null) {
                Result.Success(movieImagesResponse.body()!!)
            } else {
                val errorMessage = movieImagesResponse.errorBody()?.string() ?: "Unknown error"
                Result.Error("Movie images not found: $errorMessage")
            }
        } catch (e: Exception) {
            Result.Error("Failed to fetch movie images: ${e.message}")
        }
    }
}