package com.example.theawesomemovieapp.movieHome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theawesomemovieapp.data.MovieResponse
import com.example.theawesomemovieapp.databinding.FragmentMovieBinding

interface MovieItemListener {
    fun onItemSelected(position: Int)
}

class MyMovieRecyclerViewAdapter(
    private val listener: MovieItemListener,
) : RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder>() {

    private var values: List<MovieResponse> = ArrayList()

    fun updateData(movieList: List<MovieResponse>) {
/*        values.apply {
            clear()
            addAll(movieList)
        }*/
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
            listener.onItemSelected(position)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val binding: FragmentMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val view = binding.root

        fun bindItem(item: MovieResponse) {
            binding.movieItem = item
            // executePendingBindings() para nao esperar até o próximo ciclo de redesenho da tela
            // para atualizar a informação do item e garantir que os dados estao corretos
            binding.executePendingBindings()
        }
    }
}