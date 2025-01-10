package com.example.theawesomemovieapp.data.repository.details

import com.example.theawesomemovieapp.data.preferences.CachePreferences
import com.example.theawesomemovieapp.data.source.local.details.MovieDetailsLocalDataSource
import com.example.theawesomemovieapp.data.source.remote.details.ImageResponse
import com.example.theawesomemovieapp.data.source.remote.details.MovieDetailsRemoteDataSource
import com.example.theawesomemovieapp.data.source.remote.details.MovieImageListResponse
import com.example.theawesomemovieapp.data.source.remote.home.MovieResponse
import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.domain.model.MovieImages
import com.example.theawesomemovieapp.utils.Constants.MOVIE_DETAILS_CACHE_KEY
import com.example.theawesomemovieapp.utils.Constants.MOVIE_IMAGES_CACHE_KEY
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import java.io.IOException

class MovieDetailsRepositoryTest {

    private val remoteDataSource: MovieDetailsRemoteDataSource = mock()
    private val localDataSource: MovieDetailsLocalDataSource = mock()
    private val cachePreferences: CachePreferences = mock()

    private val movieId = "123"
    private val errorMsg = "error"

    private lateinit var repository: MovieDetailsRepositoryImpl

    @Before
    fun setUp() {
        repository = MovieDetailsRepositoryImpl(remoteDataSource, localDataSource, cachePreferences)
    }

    // --------------------- GetMovieDetails -----------------------
    @Test
    fun when_GetMovieDetails_CacheNotExpiredAndLocalNotNull_Expect_ReturnLocalData() = runTest {
        // Preparação
        val movie = prepareMockMovie()

        whenever(cachePreferences.isCacheExpired(MOVIE_DETAILS_CACHE_KEY)).thenReturn(false)
        whenever(localDataSource.getLocalMovieData(movieId.toInt())).thenReturn(movie)

        // Execução
        val result = repository.getMovieDetails(movieId)

        // Validação
        assert(result is Result.Success && result.data == movie)
        verify(cachePreferences).isCacheExpired(eq(MOVIE_DETAILS_CACHE_KEY))
        verify(localDataSource).getLocalMovieData(movieId.toInt())
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun when_GetMovieDetails_CacheIsExpired_Expect_UseRemoteAndSaveLocally() = runTest {
        // Preparação
        val movieResponse = prepareMockMovieResponse()
        val movie = prepareMockMovie()
        val resultResponse = Result.Success(movieResponse)

        whenever(cachePreferences.isCacheExpired(MOVIE_DETAILS_CACHE_KEY)).thenReturn(true)
        whenever(remoteDataSource.fetchMovieDetails(movieId)).thenReturn(resultResponse)

        // Execução
        val result = repository.getMovieDetails(movieId)

        // Validação
        assert(result is Result.Success && result.data == movie)
        verify(cachePreferences).isCacheExpired(eq(MOVIE_DETAILS_CACHE_KEY))
        verify(remoteDataSource).fetchMovieDetails(movieId)
        verify(localDataSource).saveOrUpdateMovieData(movie)
        verify(cachePreferences).setLastUpdateTime(any(), any())
    }

    @Test
    fun when_GetMovieDetails_LocalReturnsNull_Expect_UseRemoteAndSaveLocally() = runTest {
        // Preparação
        val movieResponse = prepareMockMovieResponse()
        val movie = prepareMockMovie()
        val resultResponse = Result.Success(movieResponse)

        whenever(localDataSource.getLocalMovieData(movieId.toInt())).thenReturn(null)
        whenever(remoteDataSource.fetchMovieDetails(movieId)).thenReturn(resultResponse)

        // Execução
        val result = repository.getMovieDetails(movieId)

        // Validação
        assert(result is Result.Success && result.data == movie)
        verify(remoteDataSource).fetchMovieDetails(movieId)
        verify(localDataSource).saveOrUpdateMovieData(movie)
        verify(cachePreferences).setLastUpdateTime(any(), any())
    }

    @Test
    fun when_GetMovieDetails_RemoteReturnsError_Expect_ReturnError() = runTest {
        // Preparação
        whenever(localDataSource.getLocalMovieData(movieId.toInt())).thenReturn(null)
        whenever(remoteDataSource.fetchMovieDetails(movieId)).thenReturn(Result.Error(errorMsg))

        // Execução
        val result = repository.getMovieDetails(movieId)

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
        verify(remoteDataSource).fetchMovieDetails(movieId)
        verify(localDataSource, never()).saveOrUpdateMovieData(any())
        verify(cachePreferences, never()).setLastUpdateTime(any(), any())
    }

    @Test
    fun when_GetMovieDetails_ExceptionThrown_Expect_ReturnError() = runTest {
        // Preparação
        whenever(localDataSource.getLocalMovieData(movieId.toInt()))
            .thenAnswer { throw IOException(errorMsg) }

        // Execução
        val result = repository.getMovieDetails(movieId)

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
    }

    // --------------------- GetMovieImages-----------------------
    @Test
    fun when_GetMovieImages_CacheNotExpiredAndLocalNotNull_Expect_ReturnLocalData() = runTest {
        // Preparação
        val movieImages = prepareMockMovieImages()

        whenever(cachePreferences.isCacheExpired(MOVIE_IMAGES_CACHE_KEY)).thenReturn(false)
        whenever(localDataSource.getLocalMovieImagesData(movieId.toInt())).thenReturn(movieImages)

        // Execução
        val result = repository.getMovieImages(movieId)

        // Validação
        assert(result is Result.Success && result.data == movieImages)
        verify(cachePreferences).isCacheExpired(eq(MOVIE_IMAGES_CACHE_KEY))
        verify(localDataSource).getLocalMovieImagesData(movieId.toInt())
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun when_GetMovieImages_CacheIsExpired_Expect_UseRemoteAndSaveLocally() = runTest {
        // Preparação
        val movieImagesResponse = prepareMockMovieImageListResponse()
        val movieImages = prepareMockMovieImages()
        val resultResponse = Result.Success(movieImagesResponse)

        whenever(cachePreferences.isCacheExpired(MOVIE_IMAGES_CACHE_KEY)).thenReturn(true)
        whenever(remoteDataSource.fetchMovieImages(movieId)).thenReturn(resultResponse)

        // Execução
        val result = repository.getMovieImages(movieId)

        // Validação
        assert(result is Result.Success && result.data == movieImages)
        verify(cachePreferences).isCacheExpired(eq(MOVIE_IMAGES_CACHE_KEY))
        verify(remoteDataSource).fetchMovieImages(movieId)
        verify(localDataSource).saveMovieImagesData(movieImages)
        verify(cachePreferences).setLastUpdateTime(any(), any())
    }

    @Test
    fun when_GetMovieImages_LocalReturnsNull_Expect_UseRemoteAndSaveLocally() = runTest {
        // Preparação
        val movieImagesResponse = prepareMockMovieImageListResponse()
        val movieImages = prepareMockMovieImages()
        val resultResponse = Result.Success(movieImagesResponse)

        whenever(localDataSource.getLocalMovieImagesData(movieId.toInt())).thenReturn(null)
        whenever(remoteDataSource.fetchMovieImages(movieId)).thenReturn(resultResponse)

        // Execução
        val result = repository.getMovieImages(movieId)

        // Validação
        assert(result is Result.Success && result.data == movieImages)
        verify(remoteDataSource).fetchMovieImages(movieId)
        verify(localDataSource).saveMovieImagesData(movieImages)
        verify(cachePreferences).setLastUpdateTime(any(), any())
    }

    @Test
    fun when_GetMovieImages_RemoteReturnsError_Expect_ReturnError() = runTest {
        // Preparação
        whenever(localDataSource.getLocalMovieImagesData(movieId.toInt())).thenReturn(null)
        whenever(remoteDataSource.fetchMovieImages(movieId)).thenReturn(Result.Error(errorMsg))

        // Execução
        val result = repository.getMovieImages(movieId)

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
        verify(remoteDataSource).fetchMovieImages(movieId)
        verify(localDataSource, never()).saveMovieImagesData(any())
        verify(cachePreferences, never()).setLastUpdateTime(any(), any())
    }

    @Test
    fun when_GetMovieImages_ExceptionThrown_Expect_ReturnError() = runTest {
        // Preparação
        whenever(localDataSource.getLocalMovieImagesData(movieId.toInt()))
            .thenAnswer { throw IOException(errorMsg) }

        // Execução
        val result = repository.getMovieImages(movieId)

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
    }

    private fun prepareMockMovie(): Movie {
        return Movie(movieId.toInt(), "Title", null, "content", 4.3f)
    }

    private fun prepareMockMovieResponse(): MovieResponse {
        return MovieResponse(movieId.toInt(), "Title", null, "content", 4.3f)
    }

    private fun prepareMockMovieImages(): MovieImages {
        return MovieImages(1, movieId.toInt(), listOf("imgUrl"))
    }

    private fun prepareMockMovieImageListResponse(): MovieImageListResponse {
        val imageResponse = ImageResponse("imgUrl")
        return MovieImageListResponse(1, listOf(imageResponse))
    }
}