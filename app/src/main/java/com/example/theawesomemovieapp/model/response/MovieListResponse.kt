package com.example.theawesomemovieapp.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieListResponse(
    @Json(name = "results") val movies: List<MovieResponse>
)