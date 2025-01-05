package com.example.theawesomemovieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.theawesomemovieapp.ui.model.Movie

@Dao
interface MovieDao : BaseDao<Movie> {

    @Query("SELECT * FROM Movie")
    suspend fun getAllMovies(): List<Movie>?

    @Query("SELECT * FROM Movie WHERE id=:movieId")
    suspend fun getMovieById(movieId: Int): Movie?

    @Query("DELETE FROM Movie")
    suspend fun clearMoviesData()
}