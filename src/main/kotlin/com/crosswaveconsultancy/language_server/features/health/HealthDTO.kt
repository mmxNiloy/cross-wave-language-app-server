package com.crosswaveconsultancy.language_server.features.health

enum class HealthStatus {
    UP,
    DOWN,
    OUT_OF_SERVICE,
    UNKNOWN
}

data class HealthDTO(
    val status: HealthStatus,
    val passedChecks: List<String>,
    val failedChecks: List<String>,
    val documentationUrl: String,
    val version: String,
)
