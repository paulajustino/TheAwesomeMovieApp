package com.example.theawesomemovieapp.utils

import android.view.View
import com.example.theawesomemovieapp.StateView

object Utils {

    fun showState(stateView: StateView, isLoading: Boolean, isSuccess: Boolean, isError: Boolean) {
        when {
            isLoading -> {
                stateView.progressBar.visibility = View.VISIBLE
                stateView.data.visibility = View.GONE
                stateView.errorMessage.visibility = View.GONE
            }

            isSuccess -> {
                stateView.progressBar.visibility = View.GONE
                stateView.data.visibility = View.VISIBLE
                stateView.errorMessage.visibility = View.GONE
            }

            isError -> {
                stateView.progressBar.visibility = View.GONE
                stateView.data.visibility = View.GONE
                stateView.errorMessage.visibility = View.VISIBLE
            }
        }
    }
}