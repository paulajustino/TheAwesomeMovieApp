package com.example.theawesomemovieapp.data.source.local.home

import com.example.theawesomemovieapp.data.source.local.dao.MovieDao
import com.example.theawesomemovieapp.domain.model.Movie
import javax.inject.Inject

class MoviesLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao
) : MoviesLocalDataSource {
    override suspend fun getLocalMoviesData() = movieDao.getAllMovies()

    override suspend fun saveOrUpdateMovieData(movie: Movie) = movieDao.saveOrUpdateMovie(movie)

    override suspend fun clearOldMoviesData(moviesIds: List<Int>) =
        movieDao.clearOldMovies(moviesIds)
}