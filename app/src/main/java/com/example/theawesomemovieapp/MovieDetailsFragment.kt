package com.example.theawesomemovieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.theawesomemovieapp.databinding.FragmentMovieDetailsBinding
import com.example.theawesomemovieapp.utils.Utils

class MovieDetailsFragment : Fragment(), StateView {

    private lateinit var binding: FragmentMovieDetailsBinding
    private val viewModel by navGraphViewModels<MovieViewModel>(R.id.movie_app_graph) {
        defaultViewModelProviderFactory
    }

    override val progressBar get() = binding.progressBar
    override val errorMessage get() = binding.errorMessage
    override val data get() = binding.movieDetailsCardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        binding.apply {
            lifecycleOwner = this@MovieDetailsFragment
            viewModel = this@MovieDetailsFragment.viewModel

            if (carousel.getData().isNullOrEmpty())
                carousel.visibility = View.GONE
        }

        initObservers()

        return binding.root
    }

    private fun initObservers() {
        with(viewModel) {
            appStateDetailsLiveData.observe(viewLifecycleOwner) { state ->
                state?.let {
                    Utils.showState(
                        this@MovieDetailsFragment,
                        isLoading = it == DataState.Loading,
                        isSuccess = it == DataState.Success,
                        isError = it == DataState.Error
                    )
                }
            }
        }
    }
}