package com.example.theawesomemovieapp.domain.usecase.home

import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.utils.Result

interface GetMoviesUseCase {
    suspend fun execute(): Result<List<MovieUiModel>, String>
}