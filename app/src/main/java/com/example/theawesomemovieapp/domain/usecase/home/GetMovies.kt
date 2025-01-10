package com.example.theawesomemovieapp.domain.usecase.home

import com.example.theawesomemovieapp.data.repository.home.MoviesRepository
import com.example.theawesomemovieapp.domain.mapper.MovieModelToUiModelMapper
import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.utils.Result
import javax.inject.Inject

class GetMovies @Inject constructor(
    private val moviesRepository: MoviesRepository
) : GetMoviesUseCase {
    override suspend fun execute(): Result<List<MovieUiModel>, String> {
        return try {
            when (val movies = moviesRepository.getLatestMovieList()) {
                is Result.Success -> {
                    val moviesUiModel = movies.data.map {
                        MovieModelToUiModelMapper.mapMovieModelToMovieUiModel(it)
                    }

                    Result.Success(moviesUiModel)
                }

                is Result.Error -> Result.Error(movies.error)
            }
        } catch (e: Exception) {
            Result.Error(e.message.orEmpty())
        }
    }
}