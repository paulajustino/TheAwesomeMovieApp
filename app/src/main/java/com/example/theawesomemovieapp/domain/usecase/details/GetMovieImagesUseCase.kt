package com.example.theawesomemovieapp.domain.usecase.details

import com.example.theawesomemovieapp.presentation.model.MovieImagesUiModel
import com.example.theawesomemovieapp.utils.Result

interface GetMovieImagesUseCase {
    suspend fun execute(movieId: String): Result<MovieImagesUiModel, String>
}