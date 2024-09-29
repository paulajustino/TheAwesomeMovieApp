package com.example.theawesomemovieapp

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theawesomemovieapp.data.MovieListResponse
import com.example.theawesomemovieapp.data.MovieResponse
import com.example.theawesomemovieapp.utils.DataState
import com.example.theawesomemovieapp.utils.RetrofitInstance
import com.example.theawesomemovieapp.utils.TMDBApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieHomeViewModel : ViewModel() {

    var movieListLiveData = MutableLiveData<List<MovieResponse>>()
        private set

    var navigationToMovieDetailLiveData = MutableLiveData<String>()
        private set

    var appState = MutableLiveData<DataState>()
        private set

    private val apiService = RetrofitInstance.retrofit.create(TMDBApiService::class.java)

    init {
        // Simula uma operação assíncrona + atraso de 2 segundo
        Handler(Looper.getMainLooper()).postDelayed({
            getMovieData()
        }, 2000)
    }

    fun onMovieSelected(selectedMovieId: String) {
        navigationToMovieDetailLiveData.postValue(selectedMovieId)
    }

    private fun getMovieData() {
        appState.postValue(DataState.Loading)
        apiService.getLatestMovieList(apiKey = BuildConfig.TMDB_API_KEY)
            .enqueue(object : Callback<MovieListResponse> {
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    if (response.isSuccessful) {
                        val moviesWithImage = response.body()?.movies?.filter { movie ->
                            movie.imagePath != null
                        }
                        moviesWithImage?.let {
                            movieListLiveData.postValue(it)
                            appState.postValue(DataState.Success)
                        } ?: appState.postValue(DataState.Error)
                    } else {
                        appState.postValue(DataState.Error)
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    appState.postValue(DataState.Error)
                }
            })
    }
}