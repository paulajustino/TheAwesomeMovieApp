package com.example.theawesomemovieapp.data.mapper

import com.example.theawesomemovieapp.data.source.remote.home.MovieResponse
import com.example.theawesomemovieapp.domain.model.Movie

object MovieResponseToModelMapper {
    fun mapMovieResponseToMovie(
        movieResponse: MovieResponse
    ): Movie {
        return Movie(
            id = movieResponse.id,
            title = movieResponse.title,
            posterImage = movieResponse.imagePath,
            content = movieResponse.content,
            rating = (movieResponse.rating)
        )
    }
}