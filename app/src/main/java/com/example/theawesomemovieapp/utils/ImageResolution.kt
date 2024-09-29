package com.example.theawesomemovieapp.utils

enum class ImageResolution(val rawValue: String) {

    W92("w92"),
    W154("w154"),
    W185("w185"),
    W342("w342"),
    W500("w500"),
    W780("w780"),
    ORIGINAL("original");

    companion object {
        fun getResolution(width: Int): ImageResolution {
            return when {
                width <= 92 -> W92
                width <= 154 -> W154
                width <= 185 -> W185
                width <= 342 -> W342
                width <= 500 -> W500
                width <= 780 -> W780
                else -> ORIGINAL
            }
        }
    }
}