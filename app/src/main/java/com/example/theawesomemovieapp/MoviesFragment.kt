package com.example.theawesomemovieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theawesomemovieapp.databinding.FragmentMovieListBinding
import com.example.theawesomemovieapp.placeholder.PlaceholderContent

class MoviesFragment : Fragment(), MovieItemListener {

    private var columnCount = 1
    private lateinit var binding: FragmentMovieListBinding
    val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(layoutInflater)

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        with(binding.root) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = MyMovieRecyclerViewAdapter(PlaceholderContent.ITEMS, this@MoviesFragment)
        }
    }

    override fun onItemSelected(position: Int) {
        findNavController().navigate(R.id.movieDetailsFragment)
    }
}