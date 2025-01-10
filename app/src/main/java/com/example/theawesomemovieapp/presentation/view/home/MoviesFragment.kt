package com.example.theawesomemovieapp.presentation.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theawesomemovieapp.R
import com.example.theawesomemovieapp.databinding.FragmentMovieListBinding
import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.presentation.viewmodel.home.MoviesViewModel
import com.example.theawesomemovieapp.utils.DataState
import kotlinx.coroutines.launch

class MoviesFragment : Fragment(), MovieItemListener {

    private var columnCount = 1
    private lateinit var binding: FragmentMovieListBinding
    private lateinit var adapter: MovieItemAdapter
    private val viewModel by hiltNavGraphViewModels<MoviesViewModel>(R.id.movie_app_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)
        binding.lifecycleOwner = this

        adapter = MovieItemAdapter(this)

        setupRecyclerView()
        initObservers()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.movieList.apply {
            adapter = this@MoviesFragment.adapter
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
        }
    }

    private fun initObservers() {
        // Coletando ambos os fluxos em uma única corrotina
        lifecycleScope.launch {
            // Coletando o Flow de lista de filmes
            launch {
                viewModel.moviesStateFlow.collect { state ->
                    when (state) {
                        DataState.Loading -> showLoading(true)
                        is DataState.Success-> updateMoviesUI(state.data)
                        is DataState.Error -> showError(true)
                        else -> Unit
                    }
                }
            }

            // Coletando o Flow para navegação
            launch {
                viewModel.navigationToMovieDetailsFlow.collect { movieId ->
                    movieId?.let {
                        val action =
                            MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(movieId)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onItemSelected(id: Int) {
        viewModel.onMovieSelected(id.toString())
    }

    private fun showLoading(showLoading: Boolean) {
        if (showLoading) {
            binding.apply {
                moviesProgressBar.visibility = View.VISIBLE
                movieList.visibility = View.GONE
                moviesErrorMessage.visibility = View.GONE
            }
        } else {
            binding.moviesProgressBar.visibility = View.GONE
        }
    }

    private fun showError(showError: Boolean, errorMessage: String = "") {
        if (showError) {
            binding.apply {
                moviesProgressBar.visibility = View.GONE
                movieList.visibility = View.GONE
                moviesErrorMessage.visibility = View.GONE
                moviesErrorMessage.visibility = View.VISIBLE
                //moviesErrorMessage.text = errorMessage
            }
        } else {
            binding.moviesErrorMessage.visibility = View.GONE
        }
    }

    private fun updateMoviesUI(moviesUiModel: List<MovieUiModel>) {
        showLoading(false)
        showError(false)
        binding.apply {
            movieList.visibility = View.VISIBLE
            // Atualize os dados do adaptador com a nova lista
            adapter.updateData(moviesUiModel)
        }
    }
}