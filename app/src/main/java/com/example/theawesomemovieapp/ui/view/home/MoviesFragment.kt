package com.example.theawesomemovieapp.ui.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theawesomemovieapp.R
import com.example.theawesomemovieapp.databinding.FragmentMovieListBinding
import com.example.theawesomemovieapp.ui.model.Movie
import com.example.theawesomemovieapp.ui.viewmodel.home.MoviesViewModel

class MoviesFragment : Fragment(), MovieItemListener {

    private var columnCount = 1
    private lateinit var binding: FragmentMovieListBinding
    private lateinit var adapter: MyMovieRecyclerViewAdapter
    private val viewModel by navGraphViewModels<MoviesViewModel>(R.id.movie_app_graph) {
        defaultViewModelProviderFactory
    }
    private lateinit var movieList: List<Movie>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = MyMovieRecyclerViewAdapter(this)

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
        with(viewModel) {
            movieListLiveData.observe(viewLifecycleOwner) { movieList ->
                movieList?.let {
                    adapter.updateData(it)
                    this@MoviesFragment.movieList = it
                }
            }

            navigationToMovieDetailLiveData.observe(viewLifecycleOwner) { movieId ->
                val action =
                    MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(movieId)
                findNavController().navigate(action)
            }
        }
    }

    override fun onItemSelected(position: Int) {
        val selectedMovieId = movieList[position].id.toString()
        viewModel.onMovieSelected(selectedMovieId)
    }
}