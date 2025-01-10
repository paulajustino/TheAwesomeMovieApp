package com.example.theawesomemovieapp.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.theawesomemovieapp.domain.model.MovieImages

@Dao
interface MovieImagesDao: BaseDao<MovieImages> {
    @Query("SELECT * FROM MovieImages WHERE movieId = :movieId")
    suspend fun getMovieImagesByMovieId(movieId: Int): MovieImages?
}