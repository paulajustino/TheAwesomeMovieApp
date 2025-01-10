package com.example.theawesomemovieapp.domain.home

import com.example.theawesomemovieapp.data.repository.home.MoviesRepository
import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.domain.usecase.home.GetMovies
import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetMoviesTest {
    private val repository: MoviesRepository = mock()
    private lateinit var useCase: GetMovies

    private val movieId = "123"

    @Before
    fun setUp() {
        useCase = GetMovies(repository)
    }

    @Test
    fun when_Execute_RepositoryReturnsSuccess_Expect_ReturnMovieUiModelList() =
        runTest {
            // Preparação
            val movies = prepareMockMovieList()
            val movieUiModels = prepareMockMovieUiModelList()

            whenever(repository.getLatestMovieList()).thenReturn(Result.Success(movies))

            // Execução
            val result = useCase.execute()

            // Validação
            assert(result is Result.Success && result.data == movieUiModels)
        }

    @Test
    fun when_Execute_RepositoryReturnsError_Expect_ResultError() = runTest {
        // Preparação
        val errorMsg = "Movies not found"

        whenever(repository.getLatestMovieList()).thenReturn(Result.Error(errorMsg))

        // Execução
        val result = useCase.execute()

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
    }

    @Test
    fun when_Execute_RepositoryThrowsException_Expect_ResultError() = runTest {
        // Preparação
        val exceptionMsg = "Unexpected error"

        whenever(repository.getLatestMovieList()).thenThrow(RuntimeException(exceptionMsg))

        // Execução
        val result = useCase.execute()

        // Validação
        assert(result is Result.Error && result.error == exceptionMsg)
    }

    private fun prepareMockMovieList() =
        listOf(Movie(movieId.toInt(), "Title", null, "content", 4.3f))

    private fun prepareMockMovieUiModelList() =
        listOf(MovieUiModel(movieId.toInt(), "Title", null, "content", 4.3f / 2))
}