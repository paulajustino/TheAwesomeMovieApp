package com.example.theawesomemovieapp.model.repository.details

import com.example.theawesomemovieapp.model.data.Movie
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result

interface MovieDetailsRepository {

    suspend fun getMovieDetailsAndImages(movieId: String): Result<Movie, NetworkError>
}