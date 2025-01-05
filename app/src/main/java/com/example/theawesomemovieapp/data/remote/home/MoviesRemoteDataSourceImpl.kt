package com.example.theawesomemovieapp.data.remote.home

import com.example.theawesomemovieapp.BuildConfig
import com.example.theawesomemovieapp.data.mapper.MovieResponseToModelMapper
import com.example.theawesomemovieapp.data.remote.api.RetrofitInstance
import com.example.theawesomemovieapp.data.remote.api.TMDBApiService
import com.example.theawesomemovieapp.ui.model.Movie
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result

class MoviesRemoteDataSourceImpl : MoviesRemoteDataSource {
    private val apiService = RetrofitInstance.retrofit.create(TMDBApiService::class.java)

    override suspend fun fetchLatestMovieList(): Result<List<Movie>, NetworkError> {
        return try {
            val movieListResponse =
                apiService.getLatestMovieList(apiKey = BuildConfig.TMDB_API_KEY)

            if (movieListResponse.isSuccessful) {
                movieListResponse.body()?.let { movieList ->
                    val movies =
                        MovieResponseToModelMapper.mapMovieListResponseToMovies(movieList)

                    Result.Success(movies)
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