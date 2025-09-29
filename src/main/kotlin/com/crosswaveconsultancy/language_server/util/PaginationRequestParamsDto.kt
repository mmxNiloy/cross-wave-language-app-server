package com.crosswaveconsultancy.language_server.util

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Sort

data class PaginationRequestParamsDto(
    @field:Min(1)
    val page: Int?=1,

    @field:Min(2)
    @field:Max(100)
    val limit: Int?=10,

    val search: String?="",
    val order: Sort.Direction?,
    val sort: String?
)