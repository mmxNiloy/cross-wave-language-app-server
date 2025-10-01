package com.crosswaveconsultancy.language_server.features.slide

import com.crosswaveconsultancy.language_server.features.slide.dto.SlideResponseDto
import com.crosswaveconsultancy.language_server.util.ApiError
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
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
class SlideTests {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    // List of slides test
    @Test
    fun shouldGetSlides() {
        val response = restTemplate.exchange<ApiResponsePaginated<SlideResponseDto>>(
            "/v1/slide",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponsePaginated<SlideResponseDto>>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiResponsePaginated<SlideResponseDto> = documentContext.read("$")
        data.assertValidApiResponsePaginated("/v1/slide")
        val payload: List<SlideResponseDto> = data.payload
        assertThat(payload).isNotNull
        assertThat(payload.size).isLessThanOrEqualTo(10)
        for (slide in payload) {
            slide.assertValidSlide()
        }
    }

    @Test
    fun shouldBeABadRequestForGetSlidesBecauseOfInvalidPage() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/slide?page=0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        data.assertValidError(path = "/v1/slide", status = 400)
    }

    @Test
    fun shouldBeABadRequestForGetSlidesBecauseOfInvalidLimitMin() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/slide?limit=1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        data.assertValidError(path = "/v1/slide", status = 400)
    }

    @Test
    fun shouldBeABadRequestForGetSlidesBecauseOfInvalidLimitMax() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/slide?limit=101",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        data.assertValidError(path = "/v1/slide", status = 400)
    }

    // List of slides by lesson id
    @Test
    fun shouldGetSlidesByLessonId() {
        val response = restTemplate.exchange<ApiResponsePaginated<SlideResponseDto>>(
            "/v1/slide/lesson/1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponsePaginated<SlideResponseDto>>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiResponsePaginated<SlideResponseDto> = documentContext.read("$")
        data.assertValidApiResponsePaginated("/v1/slide/lesson/1")
        val payload: List<SlideResponseDto> = data.payload
        assertThat(payload).isNotNull
        assertThat(payload.size).isLessThanOrEqualTo(10)
        for (slide in payload) {
            slide.assertValidSlide()
        }
    }

    @Test
    fun shouldBeBadRequestForGetSlidesByLessonIdBecauseOfInvalidId() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/slide/lesson/0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        data.assertValidError(path = "/v1/slide/lesson/0", status = 400)
    }

    @Test
    fun shouldBeABadRequestForGetSlidesByLessonIdBecauseOfInvalidPage() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/slide/lesson/1?page=0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        data.assertValidError(path = "/v1/slide/lesson/1", status = 400)
    }

    @Test
    fun shouldBeABadRequestForGetSlidesByLessonIdBecauseOfInvalidLimitMin() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/slide/lesson/1?limit=1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        data.assertValidError(path = "/v1/slide/lesson/1", status = 400)
    }

    @Test
    fun shouldBeABadRequestForGetSlidesByLessonIdBecauseOfInvalidLimitMax() {
        val response = restTemplate.exchange<ApiError>(
            "/v1/slide/lesson/1?limit=101",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        data.assertValidError(path = "/v1/slide/lesson/1", status = 400)
    }
}