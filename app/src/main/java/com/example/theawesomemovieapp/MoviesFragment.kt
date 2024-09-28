package com.example.theawesomemovieapp

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
import com.example.theawesomemovieapp.databinding.FragmentMovieListBinding
import com.example.theawesomemovieapp.utils.Utils

class MoviesFragment : Fragment(), MovieItemListener, StateView {

    private var columnCount = 1
    private lateinit var binding: FragmentMovieListBinding
    private lateinit var adapter: MyMovieRecyclerViewAdapter
    private val viewModel by navGraphViewModels<MovieViewModel>(R.id.movie_app_graph) {
        defaultViewModelProviderFactory
    }

    override val progressBar get() = binding.progressBar
    override val errorMessage get() = binding.errorMessage
    override val data get() = binding.movieList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)

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
            movieListLiveData.observe(viewLifecycleOwner) { adapter.updateData(it) }

            navigationToMovieDetailLiveData.observe(viewLifecycleOwner) {
                val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment()
                findNavController().navigate(action)
            }

            appStateMoviesLiveData.observe(viewLifecycleOwner) { state ->
                state?.let {
                    Utils.showState(
                        this@MoviesFragment,
                        isLoading = it == DataState.Loading,
                        isSuccess = it == DataState.Success,
                        isError = it == DataState.Error
                    )
                }
            }
        }
    }

    override fun onItemSelected(position: Int) {
        viewModel.onMovieSelected(position)
    }
}