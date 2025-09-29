package com.crosswaveconsultancy.language_server.features.health

import com.crosswaveconsultancy.language_server.util.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@RestController
@RequestMapping("/health")
class HealthController(
    private val dataSource: DataSource
) {

    @GetMapping
    fun getHealth(request: HttpServletRequest): ResponseEntity<ApiResponse<HealthDTO>> {
        val baseUrl = request.requestURL.removeSuffix(request.requestURI)
        val docsUrl = "$baseUrl/docs"

        val passedChecks = mutableListOf<String>()
        val failedChecks = mutableListOf<String>()

        // Check database connection
        try {
            dataSource.connection.use { connection ->
                if (connection.isValid(1)) {
                    passedChecks.add("Database connection OK")
                } else {
                    failedChecks.add("Database connection invalid")
                }
            }
        } catch (ex: Exception) {
            failedChecks.add("Database connection failed: ${ex.message}")
        }

        if (failedChecks.isEmpty()) {
            passedChecks.add("All other checks passed")
        }

        val payload = HealthDTO(
            status = if (failedChecks.isEmpty()) HealthStatus.UP else HealthStatus.DOWN,
            passedChecks = passedChecks,
            failedChecks = failedChecks,
            documentationUrl = docsUrl,
            version = "1.0.0"
        )

        return ResponseEntity.status(if (failedChecks.isEmpty()) HttpStatus.OK else HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse(
            message = if (failedChecks.isEmpty()) "Server is healthy" else "Server is not healthy. Errors encountered.",
            payload = payload,
            status = if (failedChecks.isEmpty()) 200 else 500,
            ok = failedChecks.isEmpty(),
        ))
    }
}