package com.example.theawesomemovieapp.domain.mapper

import com.example.theawesomemovieapp.domain.model.MovieImages
import com.example.theawesomemovieapp.presentation.model.MovieImagesUiModel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

object MovieImagesModelToUiModelMapper {
    fun mapMovieImagesModelToUiModel(
        movieImages: MovieImages
    ): MovieImagesUiModel {
        return MovieImagesUiModel(
            id = movieImages.id,
            movieId = movieImages.movieId,
            images = mapUrlsStringsToCarouselItems(movieImages.images)
        )
    }

    private fun mapUrlsStringsToCarouselItems(
        imagesUrls: List<String>
    ): List<CarouselItem> {
        val carouselImageUrlList = imagesUrls.filter { it.isNotBlank() }.take(5).map { imageUrl ->
            "https://image.tmdb.org/t/p/w500$imageUrl"
        }
        return carouselImageUrlList.map { CarouselItem(it) }
    }
}