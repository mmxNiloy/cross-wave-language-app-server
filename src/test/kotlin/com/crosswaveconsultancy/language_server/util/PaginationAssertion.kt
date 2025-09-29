package com.crosswaveconsultancy.language_server.util

import org.assertj.core.api.Assertions.assertThat
import kotlin.math.max
import kotlin.math.min


fun PaginationMetadata.assertValidPagination() {
    val pc = max(1, (totalCount + limit - 1) / limit)
    assertThat(page).isNotNull().isPositive
    assertThat(limit).isNotNull().isPositive
    assertThat(totalCount).isNotNull().isNotNegative.isGreaterThanOrEqualTo(dataCount.toLong())
    assertThat(dataCount).isNotNull().isNotNegative.isLessThanOrEqualTo(limit)
    assertThat(pageCount).isNotNull().isPositive.isEqualTo(pc)
    assertThat(first).isNotNull().isPositive.isEqualTo(1)
    assertThat(last).isNotNull().isPositive.isEqualTo(pageCount)
    assertThat(next).isNotNull().isPositive.isEqualTo(min(page + 1, pageCount))
    assertThat(previous).isNotNull().isPositive.isEqualTo(max(1, page - 1))
    assertThat(hasNext).isNotNull().isEqualTo(page < pageCount)
    assertThat(hasPrevious).isNotNull().isEqualTo(page > 1)
}