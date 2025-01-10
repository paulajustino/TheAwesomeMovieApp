package com.example.theawesomemovieapp.presentation.view

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.theawesomemovieapp.R

class MenuProviderImpl(
    private val navController: NavController,
    private val appBarConfiguration: AppBarConfiguration,
) : MenuProvider {
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        when (navController.currentDestination?.id) {
            R.id.moviesFragment -> menuInflater.inflate(R.menu.appbar_menu_movies, menu)
            R.id.movieDetailsFragment -> {
                menuInflater.inflate(R.menu.appbar_menu_movie_details, menu)
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            navController.navigateUp(appBarConfiguration)
        }
        return true
    }
}