package com.example.theawesomemovieapp.data.source.local.details

import com.example.theawesomemovieapp.data.source.local.dao.MovieDao
import com.example.theawesomemovieapp.data.source.local.dao.MovieImagesDao
import com.example.theawesomemovieapp.domain.model.Movie
import com.example.theawesomemovieapp.domain.model.MovieImages
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class MovieDetailsLocalDataSourceTest {

    private val movieDao: MovieDao = mock()
    private val movieImagesDao: MovieImagesDao = mock()
    private lateinit var localDataSource: MovieDetailsLocalDataSourceImpl

    private val movieId = 123

    @Before
    fun setUp() {
        localDataSource = MovieDetailsLocalDataSourceImpl(movieDao, movieImagesDao)
    }

    @Test
    fun when_GetLocalMovieData_Expect_MovieDaoGetMovieByIdCalled() = runTest {
        // Preparação
        val movies = prepareMockMovie()

        whenever(movieDao.getMovieById(movieId)).thenReturn(movies)

        // Execução
        val result = localDataSource.getLocalMovieData(movieId)

        // Validação
        assert(result == movies)
        verify(movieDao).getMovieById(movieId)
    }

    @Test
    fun when_GetLocalMovieImagesData_Expect_MovieImagesDaoGetMovieImagesByMovieIdCalled() =
        runTest {
            // Preparação
            val movieImages = prepareMockMovieImages()

            whenever(movieImagesDao.getMovieImagesByMovieId(movieId)).thenReturn(movieImages)

            // Execução
            val result = localDataSource.getLocalMovieImagesData(movieId)

            // Validação
            assert(result == movieImages)
            verify(movieImagesDao).getMovieImagesByMovieId(movieId)
        }

    @Test
    fun when_SaveOrUpdateMovieData_Expect_MovieDaoSaveOrUpdateMovieCalled() = runTest {
        // Preparação
        val movie = prepareMockMovie()

        whenever(movieDao.saveOrUpdateMovie(movie)).thenReturn(Unit)

        // Execução
        localDataSource.saveOrUpdateMovieData(movie)

        // Validação
        verify(movieDao).saveOrUpdateMovie(movie)
    }

    @Test
    fun when_SaveMovieImagesData_Expect_MovieImagesDaoInsertCalled() = runTest {
        // Preparação
        val movieImages = prepareMockMovieImages()

        whenever(movieImagesDao.insert(movieImages)).thenReturn(Unit)

        // Execução
        localDataSource.saveMovieImagesData(movieImages)

        // Validação
        verify(movieImagesDao).insert(movieImages)
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

    private fun prepareMockMovieImages(): MovieImages {
        return MovieImages(1, movieId, emptyList())
    }
}