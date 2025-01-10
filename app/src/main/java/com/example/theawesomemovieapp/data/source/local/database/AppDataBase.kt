package com.example.theawesomemovieapp.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.theawesomemovieapp.data.source.local.dao.MovieDao
import com.example.theawesomemovieapp.data.source.local.dao.MovieImagesDao
import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.domain.model.MovieImages
import com.example.theawesomemovieapp.utils.Converters

@Database(entities = [Movie::class, MovieImages::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieImagesDao(): MovieImagesDao
}