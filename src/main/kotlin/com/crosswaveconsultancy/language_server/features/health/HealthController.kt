package com.crosswaveconsultancy.language_server.features.health

import com.crosswaveconsultancy.language_server.util.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@Tag(name = "Health", description = "Health Check API")
@RestController
@RequestMapping("/api/health")
class HealthController(
    private val healthService: HealthService
) {

    @GetMapping
    fun getHealth(request: HttpServletRequest): ResponseEntity<ApiResponse<HealthDTO>> {
        val baseUrl = request.requestURL.removeSuffix(request.requestURI)
        val docsUrl = "$baseUrl/docs"

        val passedChecks = mutableListOf<String>()
        val failedChecks = mutableListOf<String>()
        var isDatabaseUp: Boolean = false
        val isMongoDBUp: Boolean = healthService.isMongoDBUp()

        // Check database connection
        try {
            isDatabaseUp = healthService.isDatabaseUp()
        } catch (ex: Exception) {
            isDatabaseUp = false
        }

        if(!isDatabaseUp) {
            failedChecks.add("Database connection failed")
        } else {
            passedChecks.add("Database connection successful")
        }

        if(!isMongoDBUp) {
            failedChecks.add("MongoDB connection failed")
        } else {
            passedChecks.add("MongoDB connection successful")
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

        return ResponseEntity.status(if (failedChecks.isEmpty()) HttpStatus.OK else HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponse(
                    message = if (failedChecks.isEmpty()) "Server is healthy" else "Server is not healthy. Errors encountered.",
                    payload = payload,
                    status = if (failedChecks.isEmpty()) 200 else 500,
                    ok = failedChecks.isEmpty(),
                )
            )
    }

    @GetMapping("/db")
    fun getDatabaseHealth(): ResponseEntity<ApiResponse<String>> {
        val isDatabaseUp = healthService.isDatabaseUp()

        return ResponseEntity.status(if (isDatabaseUp) HttpStatus.OK else HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiResponse(
                message = if (isDatabaseUp) "Database is healthy" else "Database is not healthy",
                payload = if (isDatabaseUp) "Database is healthy" else "Database is not healthy",
                status = if (isDatabaseUp) 200 else 500,
                ok = isDatabaseUp,
            )
        )
    }

    @GetMapping("/db/mongodb")
    fun getMongoDBHealth(): ResponseEntity<ApiResponse<String>> {
        val isMongoDBUp = healthService.isMongoDBUp()

        return ResponseEntity.status(if (isMongoDBUp) HttpStatus.OK else HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiResponse(
                message = if (isMongoDBUp) "MongoDB is healthy" else "MongoDB is not healthy",
                payload = if (isMongoDBUp) "MongoDB is healthy" else "MongoDB is not healthy",
                status = if (isMongoDBUp) 200 else 500,
                ok = isMongoDBUp,
            )
        )
    }
}