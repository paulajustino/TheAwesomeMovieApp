package com.example.theawesomemovieapp.presentation.viewmodel.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theawesomemovieapp.domain.usecase.details.GetMovieDetailsUseCase
import com.example.theawesomemovieapp.domain.usecase.details.GetMovieImagesUseCase
import com.example.theawesomemovieapp.presentation.model.MovieImagesUiModel
import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.utils.DataState
import com.example.theawesomemovieapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDetailsUseCase: GetMovieDetailsUseCase,
    private val movieImagesUseCase: GetMovieImagesUseCase
) : ViewModel() {

    var movieDetailsStateFlow =
        MutableStateFlow<DataState<MovieUiModel>>(DataState.Idle)
        private set

    var movieImagesStateFlow =
        MutableStateFlow<DataState<MovieImagesUiModel>>(DataState.Idle)
        private set

    fun getMovieDetailsWithImages(movieId: String) {
        viewModelScope.launch {
            launch {
                // Carregar detalhes do filme
                movieDetailsStateFlow.value = DataState.Loading
                val movieDetailsResult = movieDetailsUseCase.execute(movieId)
                movieDetailsStateFlow.value = when (movieDetailsResult) {
                    is Result.Success -> DataState.Success(movieDetailsResult.data)
                    is Result.Error -> DataState.Error(movieDetailsResult.error)
                }

            }

            launch {
                // Carregar imagens de forma independente
                movieImagesStateFlow.value = DataState.Loading
                val movieImagesResult = movieImagesUseCase.execute(movieId)
                movieImagesStateFlow.value = when (movieImagesResult) {
                    is Result.Success -> DataState.Success(movieImagesResult.data)
                    is Result.Error -> DataState.Error(movieImagesResult.error)
                }
            }
        }
    }
}