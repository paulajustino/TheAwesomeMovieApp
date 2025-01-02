package com.example.theawesomemovieapp.model.repository.home

import com.example.theawesomemovieapp.BuildConfig
import com.example.theawesomemovieapp.database.AppDataBase
import com.example.theawesomemovieapp.model.api.RetrofitInstance
import com.example.theawesomemovieapp.model.api.TMDBApiService
import com.example.theawesomemovieapp.model.data.Movie
import com.example.theawesomemovieapp.model.mapper.MovieResponseToModelMapper
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepositoryImpl(database: AppDataBase) : MoviesRepository {
    private val apiService = RetrofitInstance.retrofit.create(TMDBApiService::class.java)

    private val movieDao = database.movieDao()

    override suspend fun getLatestMovieList(): Result<List<Movie>, NetworkError> {
        return withContext(Dispatchers.IO) {
            try {
                val cachedMovies = loadPersistedMoviesData()
                if (!cachedMovies.isNullOrEmpty()) return@withContext Result.Success(cachedMovies)

                val movieListResponse =
                    apiService.getLatestMovieList(apiKey = BuildConfig.TMDB_API_KEY)

                if (movieListResponse.isSuccessful) {
                    movieListResponse.body()?.let { movieList ->
                        val movies =
                            MovieResponseToModelMapper.mapMovieListResponseToMovies(movieList)

                        persistMoviesData(movies)
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

    private suspend fun persistMoviesData(movieList: List<Movie>) {
        movieDao.clearMoviesData()
        movieDao.insertList(movieList)
    }

    private suspend fun loadPersistedMoviesData() = movieDao.getAllMovies()
}