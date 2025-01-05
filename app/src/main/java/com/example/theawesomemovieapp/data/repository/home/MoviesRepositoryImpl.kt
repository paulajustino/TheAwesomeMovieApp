package com.example.theawesomemovieapp.data.repository.home

import android.content.Context
import com.example.theawesomemovieapp.data.local.MoviesLocalDataSourceImpl
import com.example.theawesomemovieapp.data.remote.home.MoviesRemoteDataSourceImpl
import com.example.theawesomemovieapp.ui.model.Movie
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result

class MoviesRepositoryImpl(context: Context) : MoviesRepository {
    private val remoteDataSource = MoviesRemoteDataSourceImpl()
    private val localDataSource = MoviesLocalDataSourceImpl(context)

    override suspend fun getLatestMovieList(): Result<List<Movie>, NetworkError> {
        val localMovies = localDataSource.getLocalMoviesData()
        if (!localMovies.isNullOrEmpty())
            return Result.Success(localMovies)

        val movieListResponse = remoteDataSource.fetchLatestMovieList()
        if (movieListResponse is Result.Success) {
            persistMoviesData(movieListResponse.value)

            return movieListResponse
        } else {
            return Result.Error(NetworkError())
        }
    }

    private suspend fun persistMoviesData(movieList: List<Movie>) {
        localDataSource.clearMoviesData()
        localDataSource.saveMoviesData(movieList)
    }

}