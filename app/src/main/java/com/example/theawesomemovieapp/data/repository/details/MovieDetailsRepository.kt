package com.example.theawesomemovieapp.data.repository.details

import com.example.theawesomemovieapp.ui.model.Movie
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result

interface MovieDetailsRepository {
    suspend fun getMovieDetailsWithImages(movieId: String): Result<Movie, NetworkError>
}