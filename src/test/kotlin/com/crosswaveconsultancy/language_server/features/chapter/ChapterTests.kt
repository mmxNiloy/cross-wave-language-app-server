package com.crosswaveconsultancy.language_server.features.chapter

import com.crosswaveconsultancy.language_server.features.chapter.dto.ChapterResponseDto
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
class ChapterTests {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun shouldGetChapters() {
        val responses = restTemplate.exchange<ApiResponsePaginated<ChapterResponseDto>>(
            "/v1/chapter",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponsePaginated<ChapterResponseDto>>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.OK)

        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiResponsePaginated<ChapterResponseDto> = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidApiResponsePaginated(path="/v1/chapter")

        val payload: List<ChapterResponseDto> = data.payload
        assertThat(payload).isNotNull()
        assertThat(payload.size).isLessThanOrEqualTo(10)
        for(chapter in payload) {
            chapter.assertValidChapter()
        }
    }

    @Test
    fun shouldBeBadRequestForGetChaptersBecauseOfInvalidPage() {
        val responses = restTemplate.exchange<ApiError>(
            "/v1/chapter?page=0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/chapter", status=400)
    }

    @Test
    fun shouldBeBadRequestForGetChaptersBecauseOfInvalidLimitMin() {
        val responses = restTemplate.exchange<ApiError>(
            "/v1/chapter?limit=1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/chapter", status=400)
    }

    @Test
    fun shouldBeBadRequestForGetChaptersBecauseOfInvalidLimitMax() {
        val responses = restTemplate.exchange<ApiError>(
            "/v1/chapter?limit=101",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/chapter", status=400)
    }

    // Get chapters by course tests
    @Test
    fun shouldGetChaptersByCourse() {
        val responses = restTemplate.exchange<ApiResponsePaginated<ChapterResponseDto>>(
            "/v1/chapter/course/1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponsePaginated<ChapterResponseDto>>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.OK)
        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiResponsePaginated<ChapterResponseDto> = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidApiResponsePaginated(path="/v1/chapter/course/1")

        val payload: List<ChapterResponseDto> = data.payload
        assertThat(payload).isNotNull()
        assertThat(payload.size).isLessThanOrEqualTo(10)
        for(chapter in payload) {
            chapter.assertValidChapter()
        }
    }

    @Test
    fun shouldBeBadRequestForGetChaptersByCourseBecauseOfInvalidCourseId() {
        val responses = restTemplate.exchange<ApiError>(
            "/v1/chapter/course/0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/chapter/course/0", status=400)
    }

    @Test
    fun shouldBeBadRequestForGetChaptersByCourseBecauseOfInvalidPage() {
        val responses = restTemplate.exchange<ApiError>(
            "/v1/chapter/course/1?page=0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/chapter/course/1", status=400)
    }

    @Test
    fun shouldBeBadRequestForGetChaptersByCourseBecauseOfInvalidLimitMin() {
        val responses = restTemplate.exchange<ApiError>(
            "/v1/chapter/course/1?limit=1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/chapter/course/1", status=400)
    }

    @Test
    fun shouldBeBadRequestForGetChaptersByCourseBecauseOfInvalidLimitMax() {
        val responses = restTemplate.exchange<ApiError>(
            "/v1/chapter/course/1?limit=101",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/chapter/course/1", status=400)
    }

    // Get one chapter
    @Test
    fun shouldGetOneChapter() {
        val responses = restTemplate.exchange<ApiResponse<ChapterResponseDto>>(
            "/v1/chapter/1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponse<ChapterResponseDto>>() {}
        )
        assertThat(responses.statusCode).isEqualTo(HttpStatus.OK)
        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiResponse<ChapterResponseDto> = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidApiResponse(path="/v1/chapter/1")

        val payload: ChapterResponseDto? = data.payload
        assertThat(payload).isNotNull()
        payload!!.assertValidChapter()
    }

    @Test
    fun shouldBeBadRequestForGetOneChapterBecauseOfBadId() {
        val responses = restTemplate.exchange<ApiError>(
            "/v1/chapter/0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/chapter/0", status=400)
    }

    @Test
    fun shouldNotGetOneChapter() {
        val responses = restTemplate.exchange<ApiError>(
            "/v1/chapter/50",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(responses.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val documentContext: DocumentContext = JsonPath.parse(responses.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/chapter/50", status=404)
    }

}