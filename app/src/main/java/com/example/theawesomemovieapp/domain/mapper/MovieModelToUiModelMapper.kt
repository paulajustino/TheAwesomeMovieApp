package com.example.theawesomemovieapp.domain.mapper

import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.presentation.model.MovieUiModel

object MovieModelToUiModelMapper {
    fun mapMovieModelToMovieUiModel(
        movieModel: Movie
    ): MovieUiModel {
        return MovieUiModel(
            id = movieModel.id,
            title = movieModel.title,
            posterImage = movieModel.posterImage,
            content = movieModel.content,
            rating = (movieModel.rating) / 2
        )
    }
}