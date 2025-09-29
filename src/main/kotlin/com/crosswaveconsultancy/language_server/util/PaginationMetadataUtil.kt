package com.crosswaveconsultancy.language_server.util

import kotlin.math.max
import kotlin.math.min

fun buildPaginationMetadata(page: Int, limit: Int, dataCount: Int, totalCount: Long): PaginationMetadata {
    val pageCount = ((totalCount + limit - 1) / limit).toInt()

    return PaginationMetadata(
        page = page,
        limit = limit,
        dataCount = dataCount,
        totalCount = totalCount,
        pageCount = max(1, pageCount),
        hasNext = page < pageCount,
        hasPrevious = page > 1,
        next = max(1, min(page + 1, pageCount)),
        previous = max(page - 1, 1),
        first = 1,
        last = max(1, pageCount)
    )
}