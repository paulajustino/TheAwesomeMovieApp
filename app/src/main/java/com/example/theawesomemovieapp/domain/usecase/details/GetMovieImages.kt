package com.example.theawesomemovieapp.domain.usecase.details

import com.example.theawesomemovieapp.data.repository.details.MovieDetailsRepository
import com.example.theawesomemovieapp.domain.mapper.MovieImagesModelToUiModelMapper
import com.example.theawesomemovieapp.presentation.model.MovieImagesUiModel
import com.example.theawesomemovieapp.utils.Result
import javax.inject.Inject

class GetMovieImages @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) : GetMovieImagesUseCase {
    override suspend fun execute(movieId: String): Result<MovieImagesUiModel, String> {
        return try {
            when (val movieImages = movieDetailsRepository.getMovieImages(movieId)) {
                is Result.Success -> {
                    val movieImagesUiModel =
                        MovieImagesModelToUiModelMapper.mapMovieImagesModelToUiModel(movieImages.data)
                    Result.Success(movieImagesUiModel)
                }

                is Result.Error -> Result.Error(movieImages.error)
            }
        } catch (e: Exception) {
            Result.Error(e.message.orEmpty())
        }
    }
}