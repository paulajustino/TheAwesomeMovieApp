package com.example.theawesomemovieapp.model.mapper

import com.example.theawesomemovieapp.model.data.Movie
import com.example.theawesomemovieapp.model.response.ImageResponse
import com.example.theawesomemovieapp.model.response.MovieImageListResponse
import com.example.theawesomemovieapp.model.response.MovieListResponse
import com.example.theawesomemovieapp.model.response.MovieResponse
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

object MovieResponseToModelMapper {

    fun mapMovieResponseToMovie(
        movieResponse: MovieResponse,
        movieImageListResponse: MovieImageListResponse?
    ): Movie {
        return Movie(
            id = movieResponse.id,
            title = movieResponse.title,
            posterImage = movieResponse.imagePath,
            content = movieResponse.content,
            rating = (movieResponse.rating) / 2,
            carouselImages = mapImageListResponseToCarouselItems(movieImageListResponse?.posters)
        )
    }

    fun mapMovieListResponseToMovies(
        movieListResponse: MovieListResponse
    ): List<Movie> {
        val movieList = mutableListOf<Movie>()
        movieListResponse.movies.forEach { movieResponse ->
            movieList.add(
                Movie(
                    id = movieResponse.id,
                    title = movieResponse.title,
                    posterImage = movieResponse.imagePath,
                    content = movieResponse.content,
                    rating = (movieResponse.rating) / 2,
                    carouselImages = null
                )
            )
        }
        return movieList
    }

    private fun mapImageListResponseToCarouselItems(
        imageListResponse: List<ImageResponse>?
    ): List<CarouselItem>? {
        val carouselImageUrlList = imageListResponse?.take(5)?.map { image ->
            "https://image.tmdb.org/t/p/w500${image.filePath}"
        }
        return carouselImageUrlList?.map { CarouselItem(it) }
    }
}