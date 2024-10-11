package com.example.theawesomemovieapp.model.repository.details

import com.example.theawesomemovieapp.BuildConfig
import com.example.theawesomemovieapp.model.api.RetrofitInstance
import com.example.theawesomemovieapp.model.api.TMDBApiService
import com.example.theawesomemovieapp.model.data.Movie
import com.example.theawesomemovieapp.model.mapper.MovieResponseToModelMapper
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDetailsRepositoryImpl : MovieDetailsRepository {
    private val apiService = RetrofitInstance.retrofit.create(TMDBApiService::class.java)

    override suspend fun getMovieDetailsAndImages(movieId: String): Result<Movie, NetworkError> {
        return withContext(Dispatchers.IO) {
            try {
                val movieDetailsResponse =
                    apiService.getMovieDetails(movieId.toInt(), apiKey = BuildConfig.TMDB_API_KEY)
                val movieImagesResponse =
                    apiService.getMovieImages(movieId.toInt(), apiKey = BuildConfig.TMDB_API_KEY)

                if (movieDetailsResponse.isSuccessful) {
                    movieDetailsResponse.body()?.let { movieDetails ->
                        val movieImageList =
                            movieImagesResponse.body()?.takeIf { movieImagesResponse.isSuccessful }

                        val movieDetailsAndImages =
                            MovieResponseToModelMapper.mapMovieResponseToMovie(
                                movieDetails,
                                movieImageList
                            )
                        Result.Success(movieDetailsAndImages)
                    } ?: run {
                        Result.Error(NetworkError())
                    }
                } else {
                    Result.Error(NetworkError())
                }
            } catch (e: Exception) {
                Result.Error(NetworkError(e.message))
            }
        }
    }
}