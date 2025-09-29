package com.crosswaveconsultancy.language_server.util

data class PaginationMetadata(
    val page: Int,
    val limit: Int,
    val dataCount: Int,
    val totalCount: Long,
    val pageCount: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val next: Int,
    val previous: Int,
    val first: Int,
    val last: Int,
)
