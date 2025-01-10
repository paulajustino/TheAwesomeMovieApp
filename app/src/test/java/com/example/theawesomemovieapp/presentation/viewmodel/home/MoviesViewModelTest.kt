package com.example.theawesomemovieapp.presentation.viewmodel.home

import com.example.theawesomemovieapp.domain.usecase.home.GetMoviesUseCase
import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.utils.DataState
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {

    private val getMoviesUseCase: GetMoviesUseCase = mock()
    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MoviesViewModel

    private val stateFlow = mutableListOf<DataState<List<MovieUiModel>>>()
    private val movieId = "123"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MoviesViewModel(getMoviesUseCase, true)
    }

    @After
    fun tearDown() {
        stateFlow.clear()
        Dispatchers.resetMain()
    }

    @Test
    fun when_GetMovies_UseCaseReturnsSuccess_Expect_MoviesStateFlowSuccess() = runTest {
        // Preparação
        val moviesUiModel = listOf(MovieUiModel(1, "Title", null, "Content", 4.3f))
        val successResult = Result.Success(moviesUiModel)

        whenever(getMoviesUseCase.execute()).thenReturn(successResult)

        // Execução
        viewModel.triggerGetLatestMovieList()

        advanceUntilIdle()

        // Validação
        assert(viewModel.moviesStateFlow.value == DataState.Success(moviesUiModel))
    }

    @Test
    fun when_GetMovies_UseCaseReturnsError_Expect_MoviesStateFlowError() = runTest {
        // Preparação
        val errorMessage = "Failed to fetch movies"
        val errorResult = Result.Error(errorMessage)

        whenever(getMoviesUseCase.execute()).thenReturn(errorResult)

        // Execução
        viewModel.triggerGetLatestMovieList()

        advanceUntilIdle()

        // Validação
        assert(viewModel.moviesStateFlow.value == DataState.Error(errorMessage))
    }

    @Test
    fun when_OnMovieSelected_Expect_NavigationFlowUpdated() {
        // Execução
        viewModel.onMovieSelected(movieId)

        // Validação
        assert(viewModel.navigationToMovieDetailsFlow.value == movieId)
    }

    @Test
    fun when_GetMovies_UseCaseReturnsSuccess_Expect_StatesTransitions() = runTest {
        // Preparação
        val moviesUiModel = listOf(MovieUiModel(1, "Title", null, "Content", 4.3f))
        val successResult = Result.Success(moviesUiModel)

        whenever(getMoviesUseCase.execute()).thenReturn(successResult)

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.moviesStateFlow.collect { appState ->
                stateFlow.add(appState)
            }
        }

        // Execução
        viewModel.triggerGetLatestMovieList()

        advanceUntilIdle()

        // Validação
        assert(stateFlow[0] is DataState.Idle)
        assert(stateFlow[1] is DataState.Loading)
        assert(stateFlow[2] is DataState.Success)

        job.cancel()
    }

    @Test
    fun when_GetMovies_UseCaseReturnsError_Expect_StatesTransitions() = runTest {
        // Preparação
        val errorMessage = "Failed to fetch movies"
        val errorResult = Result.Error(errorMessage)

        whenever(getMoviesUseCase.execute()).thenReturn(errorResult)

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.moviesStateFlow.collect { appState ->
                stateFlow.add(appState)
            }
        }

        // Execução
        viewModel.triggerGetLatestMovieList()

        advanceUntilIdle()

        // Validação
        assert(stateFlow[0] is DataState.Idle)
        assert(stateFlow[1] is DataState.Loading)
        assert(stateFlow[2] is DataState.Error)

        job.cancel()
    }
}