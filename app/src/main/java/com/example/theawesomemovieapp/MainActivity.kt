package com.example.theawesomemovieapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.theawesomemovieapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var menuProvider: MenuProviderImpl
    val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        drawerLayout = binding.root

        val navHost =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHost.navController

        // configura appBarConfiguration, definindo quais telas terao o menu toolbar + drawer
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.moviesFragment), drawerLayout)

        setupToolbar()
        setupNavControllerListener()
    }

    // define que a toolbar se comporte como uma actionBar
    // vincula ao navController e às configurações da appBar
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupNavControllerListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setupMenuProvider()
            when (destination.id) {
                R.id.moviesFragment -> {
                    binding.toolbar.title = getString(R.string.app_name_title)

                }

                R.id.movieDetailsFragment -> {
                    binding.toolbar.title = ""
                }
            }
        }
    }

    private fun setupMenuProvider() {
        menuProvider = MenuProviderImpl(navController, appBarConfiguration)
        menuProvider.onCreateMenu(binding.toolbar.menu, menuInflater)
        addMenuProvider(menuProvider)
    }
}