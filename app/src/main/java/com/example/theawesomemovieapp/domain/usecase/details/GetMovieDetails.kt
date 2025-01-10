package com.example.theawesomemovieapp.domain.usecase.details

import com.example.theawesomemovieapp.data.repository.details.MovieDetailsRepository
import com.example.theawesomemovieapp.domain.mapper.MovieModelToUiModelMapper
import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.utils.Result
import javax.inject.Inject

class GetMovieDetails @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) : GetMovieDetailsUseCase {
    override suspend fun execute(movieId: String): Result<MovieUiModel, String> {
        return try {
            when (val movieDetails = movieDetailsRepository.getMovieDetails(movieId)) {
                is Result.Success -> {
                    val movieDetailsUiModel =
                        MovieModelToUiModelMapper.mapMovieModelToMovieUiModel(movieDetails.data)
                    Result.Success(movieDetailsUiModel)
                }

                is Result.Error -> Result.Error(movieDetails.error)
            }
        } catch (e: Exception) {
            Result.Error(e.message.orEmpty())
        }
    }
}