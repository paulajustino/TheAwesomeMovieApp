package com.example.theawesomemovieapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theawesomemovieapp.data.MovieImageListResponse
import com.example.theawesomemovieapp.data.MovieResponse
import com.example.theawesomemovieapp.utils.DataState
import com.example.theawesomemovieapp.utils.RetrofitInstance
import com.example.theawesomemovieapp.utils.TMDBApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsViewModel : ViewModel() {

    var movieDetailsLiveData = MutableLiveData<MovieResponse>()
        private set

    var movieImagesLiveData = MutableLiveData<List<String>>()
        private set

    private val _movieDetailsWithImagesLiveData = MediatorLiveData<Pair<MovieResponse?, List<String>?>>()
    val movieDetailsWithImagesLiveData: LiveData<Pair<MovieResponse?, List<String>?>>
        get() = _movieDetailsWithImagesLiveData

    var appState = MutableLiveData<DataState>()
        private set

    private val apiService = RetrofitInstance.retrofit.create(TMDBApiService::class.java)

    private lateinit var movieId: String

    fun setMovieId(id: String) {
        movieId = id
        _movieDetailsWithImagesLiveData.addSource(movieDetailsLiveData) { movieDetails ->
            _movieDetailsWithImagesLiveData.value = Pair(movieDetails, movieImagesLiveData.value)
        }
        _movieDetailsWithImagesLiveData.addSource(movieImagesLiveData) { movieImages ->
            _movieDetailsWithImagesLiveData.value = Pair(movieDetailsLiveData.value, movieImages)
        }
        getMovieDetails(movieId.toInt())
        getMovieImages(movieId.toInt())
    }

    private fun getMovieDetails(movieId: Int) {
        appState.postValue(DataState.Loading)
        apiService.getMovieDetails(movieId, apiKey = BuildConfig.TMDB_API_KEY)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful) {
                        movieDetailsLiveData.postValue(response.body())
                        appState.postValue(DataState.Success)

                    } else {
                        appState.postValue(DataState.Error)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    appState.postValue(DataState.Error)
                }
            })
    }

    private fun getMovieImages(movieId: Int) {
        apiService.getMovieImages(movieId, apiKey = BuildConfig.TMDB_API_KEY)
            .enqueue(object : Callback<MovieImageListResponse> {
                override fun onResponse(
                    call: Call<MovieImageListResponse>,
                    response: Response<MovieImageListResponse>
                ) {
                    val imageUrlList = response.body()?.posters?.take(5)?.map { image ->
                        "https://image.tmdb.org/t/p/w500${image.filePath}"
                    } ?: emptyList()
                    movieImagesLiveData.postValue(imageUrlList)
                }

                override fun onFailure(call: Call<MovieImageListResponse>, t: Throwable) {
                    //
                }
            })
    }
}