package com.example.theawesomemovieapp.data.source.local.details

import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.domain.model.MovieImages

interface MovieDetailsLocalDataSource {
    suspend fun getLocalMovieData(movieId: Int): Movie?
    suspend fun getLocalMovieImagesData(movieId: Int): MovieImages?
    suspend fun saveOrUpdateMovieData(movie: Movie)
    suspend fun saveMovieImagesData(movieImages: MovieImages)
}