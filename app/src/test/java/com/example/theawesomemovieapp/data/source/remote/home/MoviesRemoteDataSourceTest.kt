package com.example.theawesomemovieapp.data.source.remote.home

import com.example.theawesomemovieapp.data.source.remote.api.TMDBApiService
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MoviesRemoteDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: TMDBApiService
    private lateinit var remoteDataSource: MoviesRemoteDataSourceImpl

    private val movieId = "123"

    @Before
    fun setUp() {
        // Inicia o MockWebServer
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Cria um Retrofit com o MockWebServer
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        // Cria o serviço de API
        apiService = retrofit.create(TMDBApiService::class.java)

        remoteDataSource = MoviesRemoteDataSourceImpl(apiService)
    }

    @After
    fun tearDown() {
        // Fecha o MockWebServer após os testes
        mockWebServer.shutdown()
    }

    @Test
    fun when_FetchLatestMovieList_ApiReturnsSuccess_Expect_ReturnMovieListResponse() =
        runTest {
            // Preparação
            val jsonResponse = """
                {
                  "results": [
                    {
                      "id": 123,
                      "overview": "Content",
                      "poster_path": null,
                      "title": "Title",
                      "vote_average": 4.3
                    }
                  ]
                }
            """.trimIndent()
            val movieListResponse = prepareMockMovieListResponse()

            mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(jsonResponse))

            // Execução
            val result = remoteDataSource.fetchLatestMovieList()

            // Validação
            assert(result is Result.Success && result.data == movieListResponse)
            if (result is Result.Success) {
                assert(result.data.movies == movieListResponse.movies)
            }
        }

    @Test
    fun when_FetchLatestMovieList_ApiReturnsError_Expect_ReturnError() = runTest {
        // Preparação
        val jsonErrorResponse = """
            {
                "success": false,
                "status_code": 34,
                "status_message": "The resource you requested could not be found."
            }
        """.trimIndent()
        val errorMsg = "Movies not found: $jsonErrorResponse"

        mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(jsonErrorResponse))

        // Execução
        val result = remoteDataSource.fetchLatestMovieList()

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
    }

    private fun prepareMockMovieListResponse(): MovieListResponse {
        val moviesResponse = listOf(MovieResponse(movieId.toInt(), "Title", null, "Content", 4.3f))
        return MovieListResponse(moviesResponse)
    }
}