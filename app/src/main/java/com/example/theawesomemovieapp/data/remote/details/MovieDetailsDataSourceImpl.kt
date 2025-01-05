package com.example.theawesomemovieapp.data.remote.details

import com.example.theawesomemovieapp.BuildConfig
import com.example.theawesomemovieapp.data.mapper.MovieResponseToModelMapper
import com.example.theawesomemovieapp.data.remote.api.RetrofitInstance
import com.example.theawesomemovieapp.data.remote.api.TMDBApiService
import com.example.theawesomemovieapp.ui.model.Movie
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MovieDetailsDataSourceImpl : MovieDetailsDataSource {
    private val apiService = RetrofitInstance.retrofit.create(TMDBApiService::class.java)

    override suspend fun getMovieDetailsWithImages(movieId: String): Result<Movie, NetworkError> {
        return withContext(Dispatchers.IO) {
            try {
                val movieDetailsResponse = async {
                    apiService.getMovieDetails(movieId.toInt(), apiKey = BuildConfig.TMDB_API_KEY)
                }.await()
                val movieImagesResponse = async {
                    apiService.getMovieImages(movieId.toInt(), apiKey = BuildConfig.TMDB_API_KEY)
                }.await()

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