package com.crosswaveconsultancy.language_server.features.course

import com.crosswaveconsultancy.language_server.features.course.dto.CourseResponseDto
import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import com.crosswaveconsultancy.language_server.util.ApiError
import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.util.assertValidApiResponse
import com.crosswaveconsultancy.language_server.util.assertValidApiResponsePaginated
import com.crosswaveconsultancy.language_server.util.assertValidError
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseTests {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun shouldGetACourse() {
        val response: ResponseEntity<ApiResponse<CourseResponseDto>> = restTemplate.exchange(
            "/v1/course/1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponse<CourseResponseDto>>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiResponse<CourseResponseDto> = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidApiResponse(path="/v1/course/1")

        val payload: CourseResponseDto? = data.payload
        assertThat(payload).isNotNull()
        payload!!.assertValidCourse()
    }

    @Test
    fun shouldNotGetACourse() {
        val response: ResponseEntity<ApiError> = restTemplate.exchange(
            "/v1/course/50",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/course/50", status=404)
    }

    @Test
    fun shouldBeABadRequestForGetACourse() {
        val response: ResponseEntity<ApiError> = restTemplate.exchange(
            "/v1/course/0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/course/0", status=400)
    }

    @Test
    fun shouldBeABadRequest() {
        val response: ResponseEntity<ApiError> = restTemplate.exchange(
            "/v1/course/abc",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {})

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/course/abc", status=400, message="Invalid value for parameter 'id': abc")
    }

    @Test
    fun shouldBeABadRequestBecauseOfBadPageNumber() {
        val response: ResponseEntity<ApiError> = restTemplate.exchange(
            "/v1/course?page=0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {})

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/course", status=400)
    }

    @Test
    fun shouldBeABadRequestBecauseOfBadLimitMin() {
        val response: ResponseEntity<ApiError> = restTemplate.exchange(
            "/v1/course?limit=1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {})

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/course", status=400)
    }

    @Test
    fun shouldBeABadRequestBecauseOfBadLimitMax() {
        val response: ResponseEntity<ApiError> = restTemplate.exchange(
            "/v1/course?limit=101",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {})

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/course", status=400)
    }

    // List of courses test
    @Test
    fun shouldGetCourses() {
        val response: ResponseEntity<ApiResponsePaginated<CourseResponseDto>> = restTemplate.exchange(
            "/v1/course",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponsePaginated<CourseResponseDto>>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiResponsePaginated<CourseResponseDto> = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidApiResponsePaginated(path="/v1/course")
        val payload: List<CourseResponseDto> = data.payload
        assertThat(payload).isNotNull()
        assertThat(payload.size).isLessThanOrEqualTo(10)
        for(course in payload) {
            course.assertValidCourse()
        }
    }

    @Test
    fun shouldGetCoursesByModuleId() {
        val response: ResponseEntity<ApiResponsePaginated<CourseResponseDto>> = restTemplate.exchange(
            "/v1/course/module/1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponsePaginated<CourseResponseDto>>() {}
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiResponsePaginated<CourseResponseDto> = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidApiResponsePaginated(path="/v1/course/module/1")
        val payload: List<CourseResponseDto> = data.payload
        assertThat(payload).isNotNull()
        assertThat(payload.size).isLessThanOrEqualTo(10)
        for(course in payload) {
            course.assertValidCourse()
        }
    }

    @Test
    fun shouldBeABadRequestForCoursesByModuleId() {
        val response: ResponseEntity<ApiError> = restTemplate.exchange(
            "/v1/course/module/0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/course/module/0", status=400)
    }

    @Test
    fun shouldBeABadRequestForCoursesByModuleIdBecauseOfBadPageNumber() {
        val response: ResponseEntity<ApiError> = restTemplate.exchange(
            "/v1/course/module/1?page=0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/course/module/1", status=400)
    }

    @Test
    fun shouldBeABadRequestForCoursesByModuleIdBecauseOfBadLimitMin() {
        val response: ResponseEntity<ApiError> = restTemplate.exchange(
            "/v1/course/module/1?limit=1",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/course/module/1", status=400)
    }

    @Test
    fun shouldBeABadRequestForCoursesByModuleIdBecauseOfBadLimitMax() {
        val response: ResponseEntity<ApiError> = restTemplate.exchange(
            "/v1/course/module/1?limit=101",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiError = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidError(path="/v1/course/module/1", status=400)
    }
}
