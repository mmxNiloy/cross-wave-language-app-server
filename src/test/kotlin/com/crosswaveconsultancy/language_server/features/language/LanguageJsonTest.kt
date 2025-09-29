package com.crosswaveconsultancy.language_server.features.language

import com.crosswaveconsultancy.language_server.features.language.dto.LanguageDTO
import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import com.crosswaveconsultancy.language_server.util.ApiError
import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.util.PaginationMetadata
import com.crosswaveconsultancy.language_server.util.assertValidApiResponse
import com.crosswaveconsultancy.language_server.util.assertValidApiResponsePaginated
import com.crosswaveconsultancy.language_server.util.assertValidError
import com.crosswaveconsultancy.language_server.util.assertValidPagination
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LanguageJsonTest {
    @Autowired
    val restTemplate: TestRestTemplate? = null

    @Test
    fun shouldReturnALanguage() {
        val response: ResponseEntity<ApiResponse<LanguageResponseDto>> = restTemplate!!.exchange(
            "/v1/language/3",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponse<LanguageResponseDto>>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiResponse<LanguageResponseDto> = documentContext.read("$")
        assertThat(data).isNotNull()
        data.assertValidApiResponse(path="/v1/language/3")

        val payload: LanguageResponseDto? = data.payload

        assertThat(payload).isNotNull()
        payload!!.assertValidLanguage()
    }

    @Test
    fun shouldNotReturnALanguage() {
        val response: ResponseEntity<ApiError> = restTemplate!!.exchange(
            "/v1/language/0",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)

        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = documentContext.read("$")
        assertThat(error).isNotNull()
        error.assertValidError("/v1/language/0", 404, "Language not found with id 0")
    }

    @Test
    fun shouldBeABadRequest() {
        val response: ResponseEntity<ApiError> = restTemplate!!.exchange(
            "/v1/language/abc",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiError>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val error: ApiError = documentContext.read("$")
        assertThat(error).isNotNull
        error.assertValidError("/v1/language/abc", 400, "Invalid value for parameter 'id': abc")
    }

    @Test
    fun shouldReturnLanguages() {
        val response: ResponseEntity<ApiResponsePaginated<LanguageResponseDto>> = restTemplate!!.exchange(
            "/v1/language?limit=10",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponsePaginated<LanguageResponseDto>>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val documentContext: DocumentContext = JsonPath.parse(response.body)
        val data: ApiResponsePaginated<LanguageResponseDto> = documentContext.read("$")
        data.assertValidApiResponsePaginated("/v1/language")

        val payload: List<LanguageResponseDto> = data.payload
        assertThat(payload).isNotNull
        assertThat(payload.size).isLessThanOrEqualTo(10)

        for(language in payload) {
            language.assertValidLanguage()
        }
    }
}