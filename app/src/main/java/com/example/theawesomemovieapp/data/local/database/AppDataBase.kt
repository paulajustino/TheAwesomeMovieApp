package com.example.theawesomemovieapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.theawesomemovieapp.data.local.dao.MovieDao
import com.example.theawesomemovieapp.ui.model.Movie
import com.example.theawesomemovieapp.utils.Converters

@Database(entities = [Movie::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile // Singleton
        private var instance: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                val dataBase = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_movie_data_base"
                ).build()
                instance = dataBase
                dataBase
            }
        }
    }
}