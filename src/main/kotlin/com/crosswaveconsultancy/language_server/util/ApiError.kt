package com.crosswaveconsultancy.language_server.util

data class ApiError(
    val status: Int,
    val ok: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val message: String,
    val path: String,
    val errors: List<String>? = null // optional: field validation errors
)