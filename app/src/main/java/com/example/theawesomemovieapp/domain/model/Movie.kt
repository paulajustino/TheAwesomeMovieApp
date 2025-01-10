package com.example.theawesomemovieapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val posterImage: String?,
    val content: String,
    val rating: Float
)