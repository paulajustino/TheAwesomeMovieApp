package com.example.theawesomemovieapp.utils

sealed class Result <out D, out E> {
    data class Success <D> (val data: D) : Result<D, Nothing>()
    data class Error (val error: String) : Result<Nothing, String>()
}