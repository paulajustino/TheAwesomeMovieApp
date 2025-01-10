package com.example.theawesomemovieapp.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.theawesomemovieapp.domain.model.Movie

@Dao
interface MovieDao : BaseDao<Movie> {
    @Query("SELECT * FROM Movie")
    suspend fun getAllMovies(): List<Movie>?

    @Query("SELECT * FROM Movie WHERE id=:movieId")
    suspend fun getMovieById(movieId: Int): Movie?

    @Query("DELETE FROM Movie WHERE id NOT IN (:movieIds)")
    suspend fun clearOldMovies(movieIds: List<Int>)

    @Query("SELECT EXISTS(SELECT 1 FROM Movie WHERE id = :movieId)")
    suspend fun isMovieExists(movieId: Int): Boolean

    @Transaction
    suspend fun saveOrUpdateMovie(movie: Movie) {
        if (isMovieExists(movie.id)) {
            update(movie)
        } else {
            insert(movie)
        }
    }
}