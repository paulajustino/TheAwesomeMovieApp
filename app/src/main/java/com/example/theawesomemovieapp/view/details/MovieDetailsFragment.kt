package com.example.theawesomemovieapp.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.theawesomemovieapp.R
import com.example.theawesomemovieapp.databinding.FragmentMovieDetailsBinding
import com.example.theawesomemovieapp.utils.load
import com.example.theawesomemovieapp.viewmodel.details.MovieDetailsViewModel

class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var movieId: String
    private val viewModel by navGraphViewModels<MovieDetailsViewModel>(R.id.movie_app_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = requireArguments().getString("movieId") ?: ""
        viewModel.getMovieDetailsAndImages(movieId)
    }

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
        viewModel.movieDetailsLiveData.observe(viewLifecycleOwner) { movie ->
            movie?.let {
                with(binding) {
                    movieTitle.text = it.title
                    movieImageView.load(it.posterImage)
                    movieContent.text = it.content
                    movieRatingBar.rating = it.rating
                    it.carouselImages?.let { images ->
                        carousel.setData(images)
                        carousel.visibility = View.VISIBLE
                    } ?: run {
                        carousel.visibility = View.GONE
                    }
                }
            }

        }
    }
}