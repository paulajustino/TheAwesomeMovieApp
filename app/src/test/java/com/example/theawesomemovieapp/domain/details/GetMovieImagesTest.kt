package com.example.theawesomemovieapp.domain.details

import com.example.theawesomemovieapp.data.repository.details.MovieDetailsRepository
import com.example.theawesomemovieapp.domain.model.MovieImages
import com.example.theawesomemovieapp.domain.usecase.details.GetMovieImages
import com.example.theawesomemovieapp.presentation.model.MovieImagesUiModel
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetMovieImagesTest {

    private val repository: MovieDetailsRepository = mock()
    private lateinit var useCase: GetMovieImages

    private val movieId = "123"

    @Before
    fun setUp() {
        useCase = GetMovieImages(repository)
    }

    @Test
    fun when_Execute_RepositoryReturnsSuccess_Expect_ReturnMovieImagesUiModel() =
        runTest {
            // Preparação
            val movieImages = prepareMockMovieImages()
            val movieImagesUiModel = prepareMockMovieImagesUiModel()

            whenever(repository.getMovieImages(movieId)).thenReturn(Result.Success(movieImages))

            // Execução
            val result = useCase.execute(movieId)

            // Validação
            assert(result is Result.Success && result.data == movieImagesUiModel)
        }

    @Test
    fun when_Execute_RepositoryReturnsError_Expect_ResultError() = runTest {
        // Preparação
        val errorMsg = "Movie images not found"

        whenever(repository.getMovieImages(movieId)).thenReturn(Result.Error(errorMsg))

        // Execução
        val result = useCase.execute(movieId)

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
    }

    @Test
    fun when_Execute_RepositoryThrowsException_Expect_ResultError() = runTest {
        // Preparação
        val exceptionMsg = "Unexpected error"

        whenever(repository.getMovieImages(movieId)).thenThrow(RuntimeException(exceptionMsg))

        // Execução
        val result = useCase.execute(movieId)

        // Validação
        assert(result is Result.Error && result.error == exceptionMsg)
    }

    private fun prepareMockMovieImages() = MovieImages(1, movieId.toInt(), emptyList())

    private fun prepareMockMovieImagesUiModel() =
        MovieImagesUiModel(1, movieId.toInt(), emptyList())
}