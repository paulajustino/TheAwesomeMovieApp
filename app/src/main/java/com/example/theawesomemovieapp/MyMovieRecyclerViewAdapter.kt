package com.example.theawesomemovieapp

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.theawesomemovieapp.placeholder.PlaceholderContent.PlaceholderItem
import com.example.theawesomemovieapp.databinding.FragmentMovieBinding

interface MovieItemListener {
    fun onItemSelected(position: Int)
}

class MyMovieRecyclerViewAdapter(
    private val values: List<PlaceholderItem>,
    private val listener: MovieItemListener,
) : RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder>() {

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
        holder.idView.text = item.id
        holder.contentView.text = item.content

        holder.root.setOnClickListener {
            listener.onItemSelected(position)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.movieTitle
        val contentView: TextView = binding.movieContent
        val root: View = binding.root

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}