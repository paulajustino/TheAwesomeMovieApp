package com.example.theawesomemovieapp.data.source.remote.details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieImageListResponse(
    val id: Int,
    val posters: List<ImageResponse>
)

@JsonClass(generateAdapter = true)
data class ImageResponse(
    @Json(name = "file_path") val filePath: String?
)