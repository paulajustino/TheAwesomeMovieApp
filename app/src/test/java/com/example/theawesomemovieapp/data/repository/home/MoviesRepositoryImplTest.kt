package com.example.theawesomemovieapp.data.repository.home

import com.example.theawesomemovieapp.data.preferences.CachePreferences
import com.example.theawesomemovieapp.data.source.local.home.MoviesLocalDataSource
import com.example.theawesomemovieapp.data.source.remote.home.MovieListResponse
import com.example.theawesomemovieapp.data.source.remote.home.MovieResponse
import com.example.theawesomemovieapp.data.source.remote.home.MoviesRemoteDataSource
import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.utils.Constants.MOVIES_CACHE_KEY
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import java.io.IOException

class MoviesRepositoryImplTest {

    private val remoteDataSource: MoviesRemoteDataSource = mock()
    private val localDataSource: MoviesLocalDataSource = mock()
    private val cachePreferences: CachePreferences = mock()

    private val movieId = "123"
    private val errorMsg = "error"

    private lateinit var repository: MoviesRepositoryImpl

    @Before
    fun setUp() {
        repository = MoviesRepositoryImpl(remoteDataSource, localDataSource, cachePreferences)
    }

    @Test
    fun when_GetLatestMovieList_CacheNotExpiredAndLocalNotNull_Expect_ReturnLocalData() = runTest {
        // Preparação
        val movies = listOf(prepareMockMovie())

        whenever(cachePreferences.isCacheExpired(MOVIES_CACHE_KEY)).thenReturn(false)
        whenever(localDataSource.getLocalMoviesData()).thenReturn(movies)

        // Execução
        val result = repository.getLatestMovieList()

        // Validação
        assert(result is Result.Success && result.data == movies)
        verify(cachePreferences).isCacheExpired(eq(MOVIES_CACHE_KEY))
        verify(localDataSource).getLocalMoviesData()
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun when_GetLatestMovieList_CacheIsExpired_Expect_UseRemoteAndSaveLocally() = runTest {
        // Preparação
        val movieListResponse = prepareMockMovieListResponse()
        val movies = listOf(prepareMockMovie())
        val resultResponse = Result.Success(movieListResponse)

        whenever(cachePreferences.isCacheExpired(MOVIES_CACHE_KEY)).thenReturn(true)
        whenever(remoteDataSource.fetchLatestMovieList()).thenReturn(resultResponse)

        // Execução
        val result = repository.getLatestMovieList()

        // Validação
        assert(result is Result.Success && result.data == movies)
        verify(cachePreferences).isCacheExpired(eq(MOVIES_CACHE_KEY))
        verify(remoteDataSource).fetchLatestMovieList()
        verify(localDataSource).clearOldMoviesData(any())
        verify(localDataSource, times(movies.size)).saveOrUpdateMovieData(movies[0])
        verify(cachePreferences).setLastUpdateTime(any(), any())
    }

    @Test
    fun when_GetLatestMovieList_LocalReturnsNull_Expect_UseRemoteAndSaveLocally() = runTest {
        // Preparação
        val movieListResponse = prepareMockMovieListResponse()
        val movies = listOf(prepareMockMovie())
        val resultResponse = Result.Success(movieListResponse)

        whenever(localDataSource.getLocalMoviesData()).thenReturn(null)
        whenever(remoteDataSource.fetchLatestMovieList()).thenReturn(resultResponse)

        // Execução
        val result = repository.getLatestMovieList()

        // Validação
        assert(result is Result.Success && result.data == movies)
        verify(remoteDataSource).fetchLatestMovieList()
        verify(localDataSource).clearOldMoviesData(any())
        verify(localDataSource, times(movies.size)).saveOrUpdateMovieData(movies[0])
        verify(cachePreferences).setLastUpdateTime(any(), any())
    }

    @Test
    fun when_GetLatestMovieList_RemoteReturnsError_Expect_ReturnError() = runTest {
        // Preparação
        whenever(localDataSource.getLocalMoviesData()).thenReturn(null)
        whenever(remoteDataSource.fetchLatestMovieList()).thenReturn(Result.Error(errorMsg))

        // Execução
        val result = repository.getLatestMovieList()

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
        verify(remoteDataSource).fetchLatestMovieList()
        verify(localDataSource, never()).clearOldMoviesData(any())
        verify(localDataSource, never()).saveOrUpdateMovieData(any())
        verify(cachePreferences, never()).setLastUpdateTime(any(), any())
    }

    @Test
    fun when_GetLatestMovieList_ExceptionThrown_Expect_ReturnError() = runTest {
        // Preparação
        whenever(localDataSource.getLocalMoviesData()).thenAnswer { throw IOException(errorMsg) }

        // Execução
        val result = repository.getLatestMovieList()

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
    }

    private fun prepareMockMovie(): Movie {
        return Movie(movieId.toInt(), "Title", null, "content", 4.3f)
    }

    private fun prepareMockMovieListResponse(): MovieListResponse {
        val movieResponse = MovieResponse(movieId.toInt(), "Title", null, "content", 4.3f)
        return MovieListResponse(listOf(movieResponse))
    }
}