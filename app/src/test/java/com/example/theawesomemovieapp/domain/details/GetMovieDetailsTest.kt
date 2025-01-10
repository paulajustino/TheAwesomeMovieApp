package com.example.theawesomemovieapp.domain.details

import com.example.theawesomemovieapp.data.repository.details.MovieDetailsRepository
import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.domain.usecase.details.GetMovieDetails
import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetMovieDetailsTest {

    private val repository: MovieDetailsRepository = mock()
    private lateinit var useCase: GetMovieDetails

    private val movieId = "123"

    @Before
    fun setUp() {
        useCase = GetMovieDetails(repository)
    }

    @Test
    fun when_Execute_RepositoryReturnsSuccess_Expect_ReturnMovieUiModel() =
        runTest {
        // Preparação
        val movie = prepareMockMovie()
        val movieUiModel = prepareMockMovieUiModel()

        whenever(repository.getMovieDetails(movieId)).thenReturn(Result.Success(movie))

        // Execução
        val result = useCase.execute(movieId)

        // Validação
        assert(result is Result.Success && result.data == movieUiModel)
    }

    @Test
    fun when_Execute_RepositoryReturnsError_Expect_ResultError() = runTest {
        // Preparação
        val errorMsg = "Movie details not found"

        whenever(repository.getMovieDetails(movieId)).thenReturn(Result.Error(errorMsg))

        // Execução
        val result = useCase.execute(movieId)

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
    }

    @Test
    fun when_Execute_RepositoryThrowsException_Expect_ResultError() = runTest {
        // Preparação
        val exceptionMsg = "Unexpected error"

        whenever(repository.getMovieDetails(movieId)).thenThrow(RuntimeException(exceptionMsg))

        // Execução
        val result = useCase.execute(movieId)

        // Validação
        assert(result is Result.Error && result.error == exceptionMsg)
    }

    private fun prepareMockMovie() = Movie(movieId.toInt(), "Title", null, "content", 4.3f)

    private fun prepareMockMovieUiModel() =
        MovieUiModel(movieId.toInt(), "Title", null, "content", 4.3f / 2)
}