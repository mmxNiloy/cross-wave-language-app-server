package com.crosswaveconsultancy.language_server.features.health

import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthTest {
    @Autowired
    var restTemplate: TestRestTemplate? = null

    @Test
    fun testHealthCheck() {
        var response: ResponseEntity<ApiResponse<HealthDTO>> = restTemplate!!.exchange(
            "/health",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponse<HealthDTO>>() {}
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        var documentContext: DocumentContext = JsonPath.parse(response.body)
        var body: ApiResponse<HealthDTO> = documentContext.read("$")
        assertThat(body).isNotNull()

        val payload = body.payload
        assertThat(payload).isNotNull()

        var status: HealthStatus = payload!!.status
        var passedChecks: List<String> = payload.passedChecks
        var failedChecks: List<String> = payload.failedChecks
        var documentationUrl: String = payload.documentationUrl
        var version: String = payload.version

        assertThat(status).isEqualTo(HealthStatus.UP)
        assertThat(passedChecks).isNotEmpty()
        assertThat(failedChecks).isEmpty()
        assertThat(documentationUrl).isNotEmpty()
        assertThat(version).isNotEmpty()
    }
}