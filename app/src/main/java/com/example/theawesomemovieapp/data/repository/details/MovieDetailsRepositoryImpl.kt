package com.example.theawesomemovieapp.data.repository.details

import com.example.theawesomemovieapp.data.remote.details.MovieDetailsDataSourceImpl
import com.example.theawesomemovieapp.ui.model.Movie
import com.example.theawesomemovieapp.utils.NetworkError
import com.example.theawesomemovieapp.utils.Result

class MovieDetailsRepositoryImpl : MovieDetailsRepository {
    private val remoteDataSource = MovieDetailsDataSourceImpl()

    override suspend fun getMovieDetailsWithImages(
        movieId: String
    ): Result<Movie, NetworkError> {
        return remoteDataSource.getMovieDetailsWithImages(movieId)
    }
}
