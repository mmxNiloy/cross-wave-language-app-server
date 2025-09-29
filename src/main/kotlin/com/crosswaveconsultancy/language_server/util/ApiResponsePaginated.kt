package com.crosswaveconsultancy.language_server.util

import java.time.Instant

data class ApiResponsePaginated<T> (
    val status: Int,
    val ok: Boolean,
    val timestamp: Long = Instant.now().toEpochMilli(),
    val message: String = "",
    val payload: List<T> = emptyList(),
    val path: String = "",
    val paginationMetadata: PaginationMetadata? = null
)
