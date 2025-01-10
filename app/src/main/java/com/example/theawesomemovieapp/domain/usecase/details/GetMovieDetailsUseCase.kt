package com.example.theawesomemovieapp.domain.usecase.details

import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.utils.Result

interface GetMovieDetailsUseCase {
    suspend fun execute(movieId: String): Result<MovieUiModel, String>
}