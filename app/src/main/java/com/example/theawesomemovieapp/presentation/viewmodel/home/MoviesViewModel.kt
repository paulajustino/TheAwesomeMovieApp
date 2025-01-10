package com.example.theawesomemovieapp.presentation.viewmodel.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theawesomemovieapp.di.TestEnvironment
import com.example.theawesomemovieapp.domain.usecase.home.GetMoviesUseCase
import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.utils.DataState
import com.example.theawesomemovieapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesUseCase: GetMoviesUseCase,
    @TestEnvironment private val isTestEnvironment: Boolean
) : ViewModel() {

    var moviesStateFlow = MutableStateFlow<DataState<List<MovieUiModel>>>(DataState.Idle)
        private set

    var navigationToMovieDetailsFlow = MutableStateFlow<String?>(null)
        private set

    init {
        if (!isTestEnvironment) getMovies()
    }

    fun onMovieSelected(selectedMovieId: String) {
        navigationToMovieDetailsFlow.value = selectedMovieId
    }

    private fun getMovies() {
        viewModelScope.launch {
            // Carregar a lista de filmes
            moviesStateFlow.value = DataState.Loading
            val moviesResult = moviesUseCase.execute()
            moviesStateFlow.value = when (moviesResult) {
                is Result.Success -> DataState.Success(moviesResult.data)
                is Result.Error -> DataState.Error(moviesResult.error)
            }
        }
    }

    @VisibleForTesting
    fun triggerGetLatestMovieList() = getMovies()
}