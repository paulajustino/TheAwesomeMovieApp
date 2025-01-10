package com.example.theawesomemovieapp.data.repository.details

import com.example.theawesomemovieapp.data.source.local.details.MovieDetailsLocalDataSource
import com.example.theawesomemovieapp.data.mapper.MovieImageListResponseToModelMapper
import com.example.theawesomemovieapp.data.mapper.MovieResponseToModelMapper
import com.example.theawesomemovieapp.data.preferences.CachePreferences
import com.example.theawesomemovieapp.data.source.remote.details.MovieDetailsRemoteDataSource
import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.domain.model.MovieImages
import com.example.theawesomemovieapp.utils.Constants.MOVIE_DETAILS_CACHE_KEY
import com.example.theawesomemovieapp.utils.Constants.MOVIE_IMAGES_CACHE_KEY
import com.example.theawesomemovieapp.utils.Result
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieDetailsRemoteDataSource,
    private val localDataSource: MovieDetailsLocalDataSource,
    private val cachePreferences: CachePreferences
) : MovieDetailsRepository {
    override suspend fun getMovieDetails(movieId: String): Result<Movie, String> {
        return try {
            val localMovie = localDataSource.getLocalMovieData(movieId.toInt())
            if (!cachePreferences.isCacheExpired(MOVIE_DETAILS_CACHE_KEY) && localMovie != null) {
                Result.Success(localMovie)
            } else {
                when (val movieDetailsResponse = remoteDataSource.fetchMovieDetails(movieId)) {
                    is Result.Success -> {
                        val movieDetails =
                            MovieResponseToModelMapper
                                .mapMovieResponseToMovie(movieDetailsResponse.data)

                        persistMovieData(movieDetails)
                        updateCacheLastUpdateTime(MOVIE_DETAILS_CACHE_KEY)
                        Result.Success(movieDetails)
                    }

                    is Result.Error -> Result.Error(movieDetailsResponse.error)
                }
            }
        } catch (e: Exception) {
            Result.Error(e.message.orEmpty())
        }
    }

    override suspend fun getMovieImages(movieId: String): Result<MovieImages, String> {
        return try {
            val localMovieImages = localDataSource.getLocalMovieImagesData(movieId.toInt())
            if (!cachePreferences.isCacheExpired(MOVIE_IMAGES_CACHE_KEY) && localMovieImages != null) {
                Result.Success(localMovieImages)
            } else {
                when (val movieImagesResponse = remoteDataSource.fetchMovieImages(movieId)) {
                    is Result.Success -> {
                        val movieImages =
                            MovieImageListResponseToModelMapper.mapMovieImageListResponseToMovieImages(
                                movieId,
                                movieImagesResponse.data
                            )
                        persistMovieImagesData(movieImages)
                        updateCacheLastUpdateTime(MOVIE_IMAGES_CACHE_KEY)
                        Result.Success(movieImages)
                    }

                    is Result.Error -> Result.Error(movieImagesResponse.error)
                }
            }
        } catch (e: Exception) {
            Result.Error(e.message.orEmpty())
        }
    }

    private suspend fun persistMovieData(movie: Movie) {
        localDataSource.saveOrUpdateMovieData(movie)
    }

    private fun updateCacheLastUpdateTime(cacheKey: String) {
        cachePreferences.setLastUpdateTime(cacheKey, System.currentTimeMillis())
    }

    private suspend fun persistMovieImagesData(movieImages: MovieImages) {
        localDataSource.saveMovieImagesData(movieImages)
    }
}
