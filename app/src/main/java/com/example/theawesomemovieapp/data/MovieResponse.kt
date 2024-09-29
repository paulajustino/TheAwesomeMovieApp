package com.example.theawesomemovieapp.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResponse(
    val id: Int,
    val title: String,
    @Json(name = "poster_path") val imagePath: String?,
    @Json(name = "overview") val content: String,
    @Json(name = "vote_average") val rating: Float,
)
