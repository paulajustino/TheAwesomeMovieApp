package com.example.theawesomemovieapp.presentation.model

data class MovieUiModel(
    val id: Int,
    val title: String,
    val posterImage: String?,
    val content: String,
    val rating: Float
)
