package com.example.theawesomemovieapp.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieImageListResponse(
    val id: Int,
    val posters: List<ImageResponse>
)
