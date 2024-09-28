package com.example.theawesomemovieapp

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.theawesomemovieapp.placeholder.PlaceholderContent
import com.example.theawesomemovieapp.placeholder.PlaceholderContent.PlaceholderItem

enum class DataState {
    Loading,
    Success,
    Error
}

class MovieViewModel : ViewModel() {

    var movieDetailsLiveData = MutableLiveData<MovieDetails>()
        private set

    var movieListLiveData = MutableLiveData<MutableList<PlaceholderItem>>()
        private set

    var navigationToMovieDetailLiveData = MutableLiveData<Unit>()
        private set

    var appStateMoviesLiveData = MutableLiveData<DataState>()
        private set

    var appStateDetailsLiveData = MutableLiveData<DataState>()
        private set

    init {
        appStateMoviesLiveData.postValue(DataState.Loading)
        // Simula uma operação assíncrona + atraso de 2 segundo
        Handler(Looper.getMainLooper()).postDelayed({
            movieListLiveData.postValue(PlaceholderContent.ITEMS)
            appStateMoviesLiveData.postValue(DataState.Success)
        }, 2000)
    }

    fun onMovieSelected(position: Int) {
        navigationToMovieDetailLiveData.postValue(Unit)
        appStateDetailsLiveData.postValue(DataState.Loading)
        val movieDetails = loadMovieDetails()
        Handler(Looper.getMainLooper()).postDelayed({
            movieDetailsLiveData.postValue(movieDetails)
            appStateDetailsLiveData.postValue(DataState.Success)
        }, 2000)
    }

    private fun loadMovieDetails() : MovieDetails {
        return MovieDetails(title = "Meu filme", content = "Este é apenas um conteúdo de teste")
    }
}