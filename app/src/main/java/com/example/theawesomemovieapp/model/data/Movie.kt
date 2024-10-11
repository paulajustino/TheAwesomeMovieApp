package com.example.theawesomemovieapp.model.data

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

data class Movie(
    val id: Int,
    val title: String,
    val posterImage: String?,
    val content: String,
    val rating: Float,
    val carouselImages: List<CarouselItem>?
)