package com.example.theawesomemovieapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@Entity
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val posterImage: String?,
    val content: String,
    val rating: Float,
    val carouselImages: List<CarouselItem>?
)