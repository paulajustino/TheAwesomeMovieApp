package com.example.theawesomemovieapp.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.theawesomemovieapp.MovieDetailsViewModel
import com.example.theawesomemovieapp.R
import com.example.theawesomemovieapp.databinding.FragmentMovieDetailsBinding
import com.example.theawesomemovieapp.utils.load
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var movieId: String
    private val viewModel by navGraphViewModels<MovieDetailsViewModel>(R.id.movie_app_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = requireArguments().getString("movieId") ?: ""
        viewModel.setMovieId(movieId)
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
        viewModel.movieDetailsWithImagesLiveData.observe(viewLifecycleOwner) { (movieDetails, movieImages) ->
            if (movieDetails != null && !movieImages.isNullOrEmpty()) {
                binding.movieTitle.text = movieDetails.title
                binding.movieImageView.load(movieDetails.imagePath)
                binding.movieContent.text = movieDetails.content
                binding.movieRatingBar.rating = movieDetails.rating/2

                val carouselItems = movieImages.map { CarouselItem(it) }
                binding.carousel.setData(carouselItems)
                if (binding.carousel.getData().isNullOrEmpty())
                    binding.carousel.visibility = View.GONE
                else
                    binding.carousel.visibility = View.VISIBLE
            }
        }
    }
}