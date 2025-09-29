package com.crosswaveconsultancy.language_server.features.lesson

import com.crosswaveconsultancy.language_server.features.lesson.dto.LessonResponseDto
import com.crosswaveconsultancy.language_server.util.ApiError
import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.util.assertValidApiResponse
import com.crosswaveconsultancy.language_server.util.assertValidApiResponsePaginated
import com.crosswaveconsultancy.language_server.util.assertValidError
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LessonTests {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    // Get all lessons tests
    @Test
    fun shouldGetLessons() {
        val response = restTemplate.exchange<ApiResponsePaginated<LessonResponseDto>>(
            "/v1/lesson",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponsePaginated<LessonResponseDto>>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val document: DocumentContext = JsonPath.parse(response.body)
        val data: ApiResponsePaginated<LessonResponseDto> = document.read("$")

        assertThat(data).isNotNull

        data.assertValidApiResponsePaginated("/v1/lesson")
        val payload: List<LessonResponseDto> = data.payload

        assertThat(payload).isNotNull

        payload.forEach {
            it.assertValidLesson()
        }
    }

    @Test
    fun shouldBeBadRequestForGetLessonsBecauseOfInvalidPage() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/lesson?page=0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val document: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = document.read("$")
        error.assertValidError("/v1/lesson", 400)
    }

    @Test
    fun shouldBeBadRequestForGetLessonsBecauseOfInvalidLimitMin() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/lesson?limit=1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val document: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = document.read("$")
        error.assertValidError("/v1/lesson", 400)
    }

    @Test
    fun shouldBeBadRequestForGetLessonsBecauseOfInvalidLimitMax() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/lesson?limit=101",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val document: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = document.read("$")
        error.assertValidError("/v1/lesson", 400)
    }

    // Get lessons by chapter id tests
    @Test
    fun shouldGetLessonsByChapterId() {
        val response = restTemplate.exchange<ApiResponsePaginated<LessonResponseDto>>(
            "/v1/lesson/chapter/1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponsePaginated<LessonResponseDto>>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val document: DocumentContext = JsonPath.parse(response.body)
        val data: ApiResponsePaginated<LessonResponseDto> = document.read("$")

        data.assertValidApiResponsePaginated("/v1/lesson/chapter/1")
        val payload: List<LessonResponseDto> = data.payload

        assertThat(payload).isNotNull

        payload.forEach {
            it.assertValidLesson()
        }
    }

    @Test
    fun shouldBeBadRequestForGetLessonsByChapterIdBecauseOfInvalidChapterId() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/lesson/chapter/0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val document: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = document.read("$")
        error.assertValidError("/v1/lesson/chapter/0", 400)
    }

//    @Test
//    fun shouldNotGetLessonsByChapterId() {
//        val response = restTemplate.exchange<ApiError>(
//            "/v1/lesson/chapter/2000",
//            HttpMethod.GET,
//            null,
//            object : ParameterizedTypeReference<ApiError>() {}
//        )
//
//        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
//        val document: DocumentContext = JsonPath.parse(response.body)
//        val error: ApiError = document.read("$")
//        error.assertValidError("/v1/lesson/chapter/2000", 404)
//    }

    @Test
    fun shouldBeBadRequestForGetLessonsByChapterIdBecauseOfInvalidPage() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/lesson/chapter/1?page=0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val document: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = document.read("$")
        error.assertValidError("/v1/lesson/chapter/1", 400)
    }

    @Test
    fun shouldBeBadRequestForGetLessonsByChapterIdBecauseOfInvalidLimitMin() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/lesson/chapter/1?limit=1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val document: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = document.read("$")
        error.assertValidError("/v1/lesson/chapter/1", 400)
    }

    @Test
    fun shouldBeBadRequestForGetLessonsByChapterIdBecauseOfInvalidLimitMax() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/lesson/chapter/1?limit=101",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val document: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = document.read("$")
        error.assertValidError("/v1/lesson/chapter/1", 400)
    }

    // Get lesson by id tests
    @Test
    fun shouldGetLessonById() {
        val response = restTemplate.exchange<ApiResponse<LessonResponseDto>>(
            "/v1/lesson/1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponse<LessonResponseDto>>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val document: DocumentContext = JsonPath.parse(response.body)
        val data: ApiResponse<LessonResponseDto> = document.read("$")

        data.assertValidApiResponse("/v1/lesson/1")
        val payload: LessonResponseDto? = data.payload

        assertThat(payload).isNotNull

        payload?.assertValidLesson()
    }

    @Test
    fun shouldBeBadRequestForGetLessonByIdBecauseOfInvalidId() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/lesson/0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val document: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = document.read("$")
        error.assertValidError("/v1/lesson/0", 400)
    }

    @Test
    fun shouldNotGetLessonById() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/lesson/2000",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)

        val document: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = document.read("$")
        error.assertValidError("/v1/lesson/2000", 404)
    }

}