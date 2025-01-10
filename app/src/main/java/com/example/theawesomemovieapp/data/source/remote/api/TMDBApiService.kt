package com.example.theawesomemovieapp.data.source.remote.api

import com.example.theawesomemovieapp.data.source.remote.details.MovieImageListResponse
import com.example.theawesomemovieapp.data.source.remote.home.MovieListResponse
import com.example.theawesomemovieapp.data.source.remote.home.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {

    @GET("movie/popular")
    suspend fun getLatestMovieList(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "pt-BR",
        @Query("page") page: Int = 1
    ): Response<MovieListResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "pt-BR",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<MovieImageListResponse>
}