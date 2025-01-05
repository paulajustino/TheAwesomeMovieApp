package com.example.theawesomemovieapp.ui.viewmodel.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theawesomemovieapp.ui.model.Movie
import com.example.theawesomemovieapp.data.repository.details.MovieDetailsRepositoryImpl
import com.example.theawesomemovieapp.utils.DataState
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.launch

class MovieDetailsViewModel : ViewModel() {

    var movieDetailsLiveData = MutableLiveData<Movie>()
        private set

    var appState = MutableLiveData<DataState>()
        private set

    private val repository = MovieDetailsRepositoryImpl()

    fun getMovieDetailsAndImages(movieId: String) {
        viewModelScope.launch {
            appState.postValue(DataState.Loading)

            when (val result = repository.getMovieDetailsWithImages(movieId)) {
                is Result.Success -> {
                    movieDetailsLiveData.postValue(result.value)
                    appState.postValue(DataState.Success)
                }

                is Result.Error -> appState.postValue(DataState.Error)
            }
        }
    }
}