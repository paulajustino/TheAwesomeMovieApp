package com.example.theawesomemovieapp.data.repository.home

import com.example.theawesomemovieapp.data.mapper.MovieResponseToModelMapper
import com.example.theawesomemovieapp.data.preferences.CachePreferences
import com.example.theawesomemovieapp.data.source.local.home.MoviesLocalDataSource
import com.example.theawesomemovieapp.data.source.remote.home.MoviesRemoteDataSource
import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.utils.Constants.MOVIES_CACHE_KEY
import com.example.theawesomemovieapp.utils.Result
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val localDataSource: MoviesLocalDataSource,
    private val cachePreferences: CachePreferences
) : MoviesRepository {
    override suspend fun getLatestMovieList(): Result<List<Movie>, String> {
        return try {
            val localMovies = localDataSource.getLocalMoviesData()
            if (!cachePreferences.isCacheExpired(MOVIES_CACHE_KEY) && !localMovies.isNullOrEmpty()) {
                Result.Success(localMovies)
            } else {
                when (val movieListResponse = remoteDataSource.fetchLatestMovieList()) {
                    is Result.Success -> {
                        val movies = movieListResponse.data.movies.map {
                            MovieResponseToModelMapper.mapMovieResponseToMovie(it)
                        }
                        persistMoviesData(movies)
                        updateCacheLastUpdateTime()
                        Result.Success(movies)
                    }

                    is Result.Error -> Result.Error(movieListResponse.error)
                }
            }
        } catch (e: Exception) {
            Result.Error(e.message.orEmpty())
        }
    }

    private fun updateCacheLastUpdateTime() {
        cachePreferences.setLastUpdateTime(MOVIES_CACHE_KEY, System.currentTimeMillis())
    }

    private suspend fun persistMoviesData(movieList: List<Movie>) {
        val moviesIds = movieList.map { it.id }

        // Excluir filmes que não estão na nova lista
        localDataSource.clearOldMoviesData(moviesIds)

        // Adicionar ou atualizar os filmes da nova lista
        movieList.forEach { movie ->
            localDataSource.saveOrUpdateMovieData(movie)
        }
    }
}