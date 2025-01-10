package com.example.theawesomemovieapp.data.source.remote.details

import com.example.theawesomemovieapp.data.source.remote.api.TMDBApiService
import com.example.theawesomemovieapp.data.source.remote.home.MovieResponse
import com.example.theawesomemovieapp.utils.Result
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieDetailsRemoteDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: TMDBApiService
    private lateinit var remoteDataSource: MovieDetailsRemoteDataSourceImpl

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

        remoteDataSource = MovieDetailsRemoteDataSourceImpl(apiService)
    }

    @After
    fun tearDown() {
        // Fecha o MockWebServer após os testes
        mockWebServer.shutdown()
    }

    // --------------------- FetchMovieDetails -----------------------
    @Test
    fun when_FetchMovieDetails_ApiReturnsSuccess_Expect_ReturnMovieResponse() =
        runTest {
            // Preparação
            val jsonResponse = """
                {
                  "id": 123,
                  "overview": "Content",
                  "poster_path": null,
                  "title": "Title",
                  "vote_average": 4.3
                }
            """.trimIndent()
            val movieResponse = prepareMockMovieResponse()

            mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(jsonResponse))

            // Execução
            val result = remoteDataSource.fetchMovieDetails(movieId)

            // Validação
            assert(result is Result.Success && result.data == movieResponse)
            if (result is Result.Success) {
                assert(result.data.id == movieResponse.id)
                assert(result.data.title == movieResponse.title)
                assert(result.data.content == movieResponse.content)
                assert(result.data.imagePath == movieResponse.imagePath)
                assert(result.data.rating == movieResponse.rating)
            }
        }

    @Test
    fun when_FetchMovieDetails_ApiReturnsError_Expect_ReturnError() = runTest {
        // Preparação
        val jsonErrorResponse = """
            {
                "success": false,
                "status_code": 34,
                "status_message": "The resource you requested could not be found."
            }
        """.trimIndent()
        val errorMsg = "Movie details not found: $jsonErrorResponse"

        mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(jsonErrorResponse))

        // Execução
        val result = remoteDataSource.fetchMovieDetails(movieId)

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
    }

    // --------------------- FetchMovieImages -----------------------
    @Test
    fun when_FetchMovieImages_ApiReturnsSuccess_Expect_ReturnMovieImageListResponse() =
        runTest {
            // Preparação
            val jsonResponse = """
                {
                  "id": 1,
                  "posters": [
                    {
                      "file_path": "imgUrl"
                    }
                  ]
                }
            """.trimIndent()
            val movieImagesResponse = prepareMockMovieImageListResponse()

            mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(jsonResponse))

            // Execução
            val result = remoteDataSource.fetchMovieImages(movieId)

            // Validação
            assert(result is Result.Success && result.data == movieImagesResponse)
            if (result is Result.Success) {
                assert(result.data.id == movieImagesResponse.id)
                assert(result.data.posters == movieImagesResponse.posters)
            }
        }

    @Test
    fun when_FetchMovieImages_ApiReturnsError_Expect_ReturnError() = runTest {
        // Preparação
        val jsonErrorResponse = """
            {
                "success": false,
                "status_code": 34,
                "status_message": "The resource you requested could not be found."
            }
        """.trimIndent()
        val errorMsg = "Movie images not found: $jsonErrorResponse"

        mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(jsonErrorResponse))

        // Execução
        val result = remoteDataSource.fetchMovieImages(movieId)

        // Validação
        assert(result is Result.Error && result.error == errorMsg)
    }

    private fun prepareMockMovieResponse(): MovieResponse {
        return MovieResponse(movieId.toInt(), "Title", null, "Content", 4.3f)
    }

    private fun prepareMockMovieImageListResponse(): MovieImageListResponse {
        val imageResponse = ImageResponse("imgUrl")
        return MovieImageListResponse(1, listOf(imageResponse))
    }
}