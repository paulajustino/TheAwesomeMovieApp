package com.example.theawesomemovieapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.theawesomemovieapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var menuProvider: MenuProviderImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView

        setSupportActionBar(binding.toolbar)

        val navHost =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as? NavHostFragment
        navController = navHost?.navController ?: return

        /* appBar configuration define em quais destinos vai mostrar o drawer e o voltar
            destinos de nivel superior = mostra o drawer
            destinos de nivel inferior = mostra o voltar */
        appBarConfiguration = AppBarConfiguration(setOf(R.id.moviesFragment), drawerLayout)

        /* o navController é responsável por gerenciar a navegação do app
           quando vinculado ao navigationMenu ou actionBar
           os itens e botoes dos menus ficam interativos
           o navController sabe pra qual destino transitar quando algum é selecionado */
        navigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setupNavControllerListener()
    }

    private fun setupNavControllerListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setupMenuProvider()
            when (destination.id) {
                R.id.moviesFragment -> {
                    binding.toolbarTitle = getString(R.string.app_name_title)

                }

                R.id.movieDetailsFragment -> {
                    binding.toolbarTitle = ""
                }
            }
        }
    }

    private fun setupMenuProvider() {
        menuProvider = MenuProviderImpl(navController, appBarConfiguration)
        menuProvider.onCreateMenu(binding.toolbar.menu, menuInflater)
        addMenuProvider(menuProvider)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}