package com.example.theawesomemovieapp.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResponse(
    @Json(name = "file_path") val filePath: String
)
