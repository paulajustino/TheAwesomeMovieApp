package com.example.theawesomemovieapp.presentation.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theawesomemovieapp.databinding.FragmentMovieBinding
import com.example.theawesomemovieapp.presentation.model.MovieUiModel
import com.example.theawesomemovieapp.utils.load

interface MovieItemListener {
    fun onItemSelected(id: Int)
}

class MovieItemAdapter(
    private val listener: MovieItemListener,
) : RecyclerView.Adapter<MovieItemAdapter.ViewHolder>() {

    private var values = emptyList<MovieUiModel>()

    fun updateData(movieList: List<MovieUiModel>) {
        values = movieList
        notifyDataSetChanged()
    }

    // infla a view do item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    // carrega as informações na view do item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        holder.bindItem(item)
        holder.view.setOnClickListener {
            listener.onItemSelected(values[position].id)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val binding: FragmentMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val view = binding.root

        fun bindItem(item: MovieUiModel) {
            binding.apply {
                movieImageView.load(item.posterImage)
                movieTitle.text = item.title
                movieContent.text = item.content
            }

            // executePendingBindings() para nao esperar até o próximo ciclo de redesenho da tela
            // para atualizar a informação do item e garantir que os dados estao corretos
            binding.executePendingBindings()
        }
    }
}