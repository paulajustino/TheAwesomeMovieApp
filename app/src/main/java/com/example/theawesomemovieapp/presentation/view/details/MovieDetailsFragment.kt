package com.example.theawesomemovieapp.presentation.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.example.theawesomemovieapp.R
import com.example.theawesomemovieapp.databinding.FragmentMovieDetailsBinding
import com.example.theawesomemovieapp.presentation.model.MovieImagesUiModel
import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.presentation.viewmodel.details.MovieDetailsViewModel
import com.example.theawesomemovieapp.utils.DataState
import com.example.theawesomemovieapp.utils.load
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var movieId: String
    private val viewModel by hiltNavGraphViewModels<MovieDetailsViewModel>(R.id.movie_app_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = requireArguments().getString("movieId") ?: ""
        viewModel.getMovieDetailsWithImages(movieId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        binding.lifecycleOwner = this

        initObservers()

        return binding.root
    }

    private fun initObservers() {
        // Coletando ambos os fluxos em uma única corrotina
        lifecycleScope.launch {
            // Coletar estado dos detalhes do filme
            launch {
                viewModel.movieDetailsStateFlow.collect { state ->
                    when (state) {
                        DataState.Loading -> showLoadingForDetails(true)
                        is DataState.Success -> updateMovieDetailsUI(state.data)
                        is DataState.Error -> showErrorForDetails(true, state.message)
                        else -> Unit
                    }
                }
            }

            // Coletar estado das imagens do filme
            launch {
                viewModel.movieImagesStateFlow.collect { state ->
                    when (state) {
                        DataState.Loading -> showLoadingForImages(true)
                        is DataState.Success -> updateMovieImagesUI(state.data)
                        is DataState.Error -> showErrorForImages(true, state.message)
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showLoadingForDetails(showLoading: Boolean) {
        if (showLoading) {
            binding.apply {
                detailsProgressBar.visibility = View.VISIBLE
                movieDetailsCardView.visibility = View.GONE
                detailsErrorMessage.visibility = View.GONE
            }
        } else {
            binding.detailsProgressBar.visibility = View.GONE
        }
    }

    private fun showErrorForDetails(showError: Boolean, errorMessage: String = "") {
        if (showError) {
            binding.apply {
                detailsProgressBar.visibility = View.GONE
                movieDetailsCardView.visibility = View.GONE
                detailsErrorMessage.visibility = View.VISIBLE
                //detailsErrorMessage.text = errorMessage
            }
        } else {
            binding.detailsErrorMessage.visibility = View.GONE
        }
    }

    private fun updateMovieDetailsUI(movieUiModel: MovieUiModel) {
        showLoadingForDetails(false)
        showErrorForDetails(false)
        binding.apply {
            movieDetailsCardView.visibility = View.VISIBLE
            movieTitle.text = movieUiModel.title
            movieImageView.load(movieUiModel.posterImage)
            movieContent.text = movieUiModel.content
            movieRatingBar.rating = movieUiModel.rating
        }
    }

    private fun showLoadingForImages(showLoading: Boolean) {
        if (showLoading) {
            binding.apply {
                imagesProgressBar.visibility = View.VISIBLE
                carouselImages.visibility = View.GONE
                imagesErrorMessage.visibility = View.GONE
            }
        } else {
            binding.imagesProgressBar.visibility = View.GONE
        }
    }

    private fun showErrorForImages(showError: Boolean, errorMessage: String = "") {
        if (showError) {
            binding.apply {
                imagesProgressBar.visibility = View.GONE
                carouselImages.visibility = View.GONE
                imagesErrorMessage.visibility = View.VISIBLE
                //imagesErrorMessage.text = errorMessage
            }
        } else {
            binding.imagesErrorMessage.visibility = View.GONE
        }
    }

    private fun updateMovieImagesUI(movieImagesUiModel: MovieImagesUiModel) {
        showLoadingForImages(false)
        showErrorForImages(false)
        binding.carouselImages.apply {
            if (movieImagesUiModel.images.isNotEmpty()) {
                setData(movieImagesUiModel.images)
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
    }
}