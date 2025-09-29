package com.crosswaveconsultancy.language_server.util

import org.assertj.core.api.Assertions.assertThat

fun <T> ApiResponse<T>.assertValidApiResponse(path: String?) {
    assertThat(ok).isTrue
    assertThat(this.path).isNotNull
    assertThat(status).isNotNull.isGreaterThanOrEqualTo(200).isLessThanOrEqualTo(299)
    assertThat(timestamp).isNotNull
    assertThat(message).isNotNull.isNotEmpty
    assertThat(payload).isNotNull

    if (path != null) assertThat(this.path).isEqualTo(path)
}

fun <T> ApiResponsePaginated<T>.assertValidApiResponsePaginated(path: String?) {
    assertThat(ok).isTrue
    assertThat(status).isNotNull.isGreaterThanOrEqualTo(200).isLessThanOrEqualTo(299)
    assertThat(this.path).isNotNull
    assertThat(timestamp).isNotNull
    assertThat(message).isNotNull.isNotEmpty
    assertThat(payload).isNotNull
    assertThat(paginationMetadata).isNotNull
    paginationMetadata!!.assertValidPagination()

    if (path != null) assertThat(this.path).isEqualTo(path)
}