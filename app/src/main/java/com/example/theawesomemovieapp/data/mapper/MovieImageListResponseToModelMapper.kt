package com.example.theawesomemovieapp.data.mapper

import com.example.theawesomemovieapp.data.source.remote.details.MovieImageListResponse
import com.example.theawesomemovieapp.domain.model.MovieImages

object MovieImageListResponseToModelMapper {
    fun mapMovieImageListResponseToMovieImages(
        movieId: String,
        movieImageListResponse: MovieImageListResponse
    ): MovieImages {
        return MovieImages(
            id = movieImageListResponse.id,
            movieId = movieId.toInt(),
            images = movieImageListResponse.posters.map { it.filePath.orEmpty() }
        )
    }
}