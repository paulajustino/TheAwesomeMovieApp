package com.example.theawesomemovieapp.presentation.model

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

data class MovieImagesUiModel(
    val id: Int,
    val movieId: Int,
    val images: List<CarouselItem>
)
