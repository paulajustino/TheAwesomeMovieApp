package com.example.theawesomemovieapp.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.theawesomemovieapp.database.AppDataBase
import com.example.theawesomemovieapp.model.data.Movie
import com.example.theawesomemovieapp.model.repository.home.MoviesRepositoryImpl
import com.example.theawesomemovieapp.utils.DataState
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.launch

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    var movieListLiveData = MutableLiveData<List<Movie>>()
        private set

    var navigationToMovieDetailLiveData = MutableLiveData<String>()
        private set

    var appState = MutableLiveData<DataState>()
        private set

    private val appDataBase = AppDataBase.getDataBase(application)
    private val repository = MoviesRepositoryImpl(appDataBase)

    init {
        getLatestMovieList()
    }

    fun onMovieSelected(selectedMovieId: String) {
        navigationToMovieDetailLiveData.postValue(selectedMovieId)
    }

    private fun getLatestMovieList() {
        appState.postValue(DataState.Loading)

        viewModelScope.launch {
            appState.postValue(DataState.Loading)

            when (val result = repository.getLatestMovieList()) {
                is Result.Success -> {
                    movieListLiveData.postValue(result.value)
                    appState.postValue(DataState.Success)
                }

                is Result.Error -> appState.postValue(DataState.Error)
            }
        }
    }
}