package com.example.theawesomemovieapp.data.repository.details

import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.domain.model.MovieImages
import com.example.theawesomemovieapp.utils.Result

interface MovieDetailsRepository {
    suspend fun getMovieDetails(movieId: String): Result<Movie, String>
    suspend fun getMovieImages(movieId: String): Result<MovieImages, String>
}