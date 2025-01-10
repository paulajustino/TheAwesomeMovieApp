package com.example.theawesomemovieapp.presentation.viewmodel.details

import com.example.theawesomemovieapp.domain.usecase.details.GetMovieDetailsUseCase
import com.example.theawesomemovieapp.domain.usecase.details.GetMovieImagesUseCase
import com.example.theawesomemovieapp.presentation.model.MovieImagesUiModel
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
class MovieDetailsViewModelTest {

    private val getMovieDetailsUseCase: GetMovieDetailsUseCase = mock()
    private val getMovieImagesUseCase: GetMovieImagesUseCase = mock()

    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MovieDetailsViewModel

    private val detailsStateFlow = mutableListOf<DataState<MovieUiModel>>()
    private val imagesStateFlow = mutableListOf<DataState<MovieImagesUiModel>>()
    private val movieId = "123"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieDetailsViewModel(getMovieDetailsUseCase, getMovieImagesUseCase)
    }

    @After
    fun tearDown() {
        detailsStateFlow.clear()
        imagesStateFlow.clear()
        Dispatchers.resetMain()
    }

    @Test
    fun when_GetMovieDetailsWithImages_UseCasesReturnSuccess_Expect_StateFlowsSuccess() =
        runTest {
            // Preparação
            val movieUiModel = MovieUiModel(1, "Title", null, "Content", 4.3f)
            val movieImagesUiModel = MovieImagesUiModel(1, movieId.toInt(), emptyList())
            val successDetailsResult = Result.Success(movieUiModel)
            val successImagesResult = Result.Success(movieImagesUiModel)

            whenever(getMovieDetailsUseCase.execute(movieId)).thenReturn(successDetailsResult)
            whenever(getMovieImagesUseCase.execute(movieId)).thenReturn(successImagesResult)

            // Execução
            viewModel.getMovieDetailsWithImages(movieId)

            advanceUntilIdle()

            // Validação
            assert(viewModel.movieDetailsStateFlow.value == DataState.Success(movieUiModel))
            assert(viewModel.movieImagesStateFlow.value == DataState.Success(movieImagesUiModel))
        }

    @Test
    fun when_GetMovieDetailsWithImages_UseCasesReturnError_Expect_StateFlowsError() =
        runTest {
            val detailsMsgError = "Failed to fetch movie details"
            val imagesMsgError = "Failed to fetch movie images"
            val errorDetailsResult = Result.Error(detailsMsgError)
            val errorImagesResult = Result.Error(imagesMsgError)

            whenever(getMovieDetailsUseCase.execute(movieId)).thenReturn(errorDetailsResult)
            whenever(getMovieImagesUseCase.execute(movieId)).thenReturn(errorImagesResult)

            // Execução
            viewModel.getMovieDetailsWithImages(movieId)

            advanceUntilIdle()

            // Validação
            assert(viewModel.movieDetailsStateFlow.value == DataState.Error(detailsMsgError))
            assert(viewModel.movieImagesStateFlow.value == DataState.Error(imagesMsgError))
        }

    @Test
    fun when_GetMovieDetailsWithImages_UseCasesReturnSuccess_Expect_StatesTransitions() = runTest {
        // Preparação
        val movieUiModel = MovieUiModel(1, "Title", null, "Content", 4.3f)
        val movieImagesUiModel = MovieImagesUiModel(1, movieId.toInt(), emptyList())
        val successDetailsResult = Result.Success(movieUiModel)
        val successImagesResult = Result.Success(movieImagesUiModel)

        whenever(getMovieDetailsUseCase.execute(movieId)).thenReturn(successDetailsResult)
        whenever(getMovieImagesUseCase.execute(movieId)).thenReturn(successImagesResult)

        val movieDetailsJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieDetailsStateFlow.collect { state ->
                detailsStateFlow.add(state)
            }
        }

        val movieImagesJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieImagesStateFlow.collect { state ->
                imagesStateFlow.add(state)
            }
        }

        // Execução
        viewModel.getMovieDetailsWithImages(movieId)

        advanceUntilIdle()

        // Validação
        assert(detailsStateFlow[0] is DataState.Idle)
        assert(detailsStateFlow[1] is DataState.Loading)
        assert(detailsStateFlow[2] is DataState.Success)
        assert(imagesStateFlow[0] is DataState.Idle)
        assert(imagesStateFlow[1] is DataState.Loading)
        assert(imagesStateFlow[2] is DataState.Success)

        movieDetailsJob.cancel()
        movieImagesJob.cancel()
    }

    @Test
    fun when_GetMovieDetailsWithImages_UseCasesReturnError_Expect_StatesTransitions() = runTest {
        // Preparação
        val detailsMsgError = "Failed to fetch movie details"
        val imagesMsgError = "Failed to fetch movie images"
        val errorDetailsResult = Result.Error(detailsMsgError)
        val errorImagesResult = Result.Error(imagesMsgError)

        whenever(getMovieDetailsUseCase.execute(movieId)).thenReturn(errorDetailsResult)
        whenever(getMovieImagesUseCase.execute(movieId)).thenReturn(errorImagesResult)

        val movieDetailsJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieDetailsStateFlow.collect { state ->
                detailsStateFlow.add(state)
            }
        }

        val movieImagesJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieImagesStateFlow.collect { state ->
                imagesStateFlow.add(state)
            }
        }

        // Execução
        viewModel.getMovieDetailsWithImages(movieId)

        advanceUntilIdle()

        // Validação
        assert(detailsStateFlow[0] is DataState.Idle)
        assert(detailsStateFlow[1] is DataState.Loading)
        assert(detailsStateFlow[2] is DataState.Error)
        assert(imagesStateFlow[0] is DataState.Idle)
        assert(imagesStateFlow[1] is DataState.Loading)
        assert(imagesStateFlow[2] is DataState.Error)

        movieDetailsJob.cancel()
        movieImagesJob.cancel()
    }
}