package com.example.theawesomemovieapp.data.source.local.details

import com.example.theawesomemovieapp.data.source.local.dao.MovieDao
import com.example.theawesomemovieapp.data.source.local.dao.MovieImagesDao
import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.domain.model.MovieImages
import javax.inject.Inject

class MovieDetailsLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val movieImagesDao: MovieImagesDao
): MovieDetailsLocalDataSource {
    override suspend fun getLocalMovieData(movieId: Int): Movie? = movieDao.getMovieById(movieId)

    override suspend fun getLocalMovieImagesData(movieId: Int) =
        movieImagesDao.getMovieImagesByMovieId(movieId)

    override suspend fun saveOrUpdateMovieData(movie: Movie) = movieDao.saveOrUpdateMovie(movie)

    override suspend fun saveMovieImagesData(movieImages: MovieImages) {
        movieImagesDao.insert(movieImages)
    }
}