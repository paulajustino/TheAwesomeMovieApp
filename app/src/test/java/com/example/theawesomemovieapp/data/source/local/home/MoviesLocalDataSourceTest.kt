package com.example.theawesomemovieapp.data.source.local.home

import com.example.theawesomemovieapp.data.source.local.dao.MovieDao
import com.example.theawesomemovieapp.domain.model.Movie
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class MoviesLocalDataSourceTest {

    private val movieDao: MovieDao = mock()
    private lateinit var localDataSource: MoviesLocalDataSourceImpl

    @Before
    fun setUp() {
        localDataSource = MoviesLocalDataSourceImpl(movieDao)
    }

    @Test
    fun when_GetLocalMoviesData_Expect_MovieDaoGetAllMoviesCalled() = runTest {
        // Preparação
        val movies = listOf(prepareMockMovie())

        whenever(movieDao.getAllMovies()).thenReturn(movies)

        // Execução
        val result = localDataSource.getLocalMoviesData()

        // Validação
        assert(result == movies)
        verify(movieDao).getAllMovies()
    }

    @Test
    fun when_SaveOrUpdateMovieData_Expect_MovieDaoSaveOrUpdateCalled() = runTest {
        // Preparação
        val movie = prepareMockMovie()

        whenever(movieDao.saveOrUpdateMovie(movie)).thenReturn(Unit)

        // Execução
        localDataSource.saveOrUpdateMovieData(movie)

        // Validação
        verify(movieDao).saveOrUpdateMovie(movie)
    }

    @Test
    fun when_ClearOldMoviesData_Expect_MovieDaoClearOldMoviesCalled() = runTest {
        // Preparação
        val moviesIds = listOf(1, 2, 3)

        whenever(movieDao.clearOldMovies(moviesIds)).thenReturn(Unit)

        // Execução
        localDataSource.clearOldMoviesData(moviesIds)

        // Validação
        verify(movieDao).clearOldMovies(moviesIds)
    }

    private fun prepareMockMovie(): Movie {
        return Movie(
            id = 1,
            title = "Movie 1",
            posterImage = null,
            content = "Content Movie 1",
            rating = 4.0f
        )
    }
}