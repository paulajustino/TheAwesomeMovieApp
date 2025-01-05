package com.example.theawesomemovieapp.data.local

import android.content.Context
import com.example.theawesomemovieapp.data.local.database.AppDataBase
import com.example.theawesomemovieapp.ui.model.Movie

class MoviesLocalDataSourceImpl(context: Context) : MoviesLocalDataSource {
    private val database = AppDataBase.getDataBase(context)
    private val movieDao = database.movieDao()

    override suspend fun getLocalMoviesData() = movieDao.getAllMovies()

    override suspend fun saveMoviesData(movieList: List<Movie>) {
        movieDao.insertList(movieList)
    }

    override suspend fun clearMoviesData() {
        movieDao.clearMoviesData()
    }
}