package com.crosswaveconsultancy.language_server.util

import java.time.Instant

data class ApiResponse<T>(
    val status: Int,
    val ok: Boolean,
    val timestamp: Long = Instant.now().toEpochMilli(),
    val message: String = "",
    val payload: T? = null,
    val path: String = "",
)