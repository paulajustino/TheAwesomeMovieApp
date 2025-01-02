package com.example.theawesomemovieapp.model.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.theawesomemovieapp.model.data.Movie

@Dao
interface MovieDao : BaseDao<Movie> {

    @Query("SELECT * FROM movie")
    suspend fun getAllMovies(): List<Movie>?

    @Query("SELECT * FROM movie WHERE id=:movieId")
    suspend fun getMovieById(movieId: Int): Movie?

    @Query("DELETE FROM movie")
    suspend fun clearMoviesData()
}