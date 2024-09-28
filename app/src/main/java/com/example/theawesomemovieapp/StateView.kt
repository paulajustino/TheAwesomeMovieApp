package com.example.theawesomemovieapp

import android.view.View

interface StateView {
    val progressBar: View
    val errorMessage: View
    val data: View
}