package com.example.theawesomemovieapp.utils

object ImageBuilder {
    private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/"

    fun build(posterPath : String, size: Int) : String {
        val resolution = ImageResolution.getResolution(width = size.dp).rawValue
        return POSTER_BASE_URL + resolution + posterPath
    }
}