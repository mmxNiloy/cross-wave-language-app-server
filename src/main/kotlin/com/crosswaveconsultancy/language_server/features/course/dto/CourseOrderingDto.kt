package com.crosswaveconsultancy.language_server.features.course.dto

import jakarta.validation.constraints.Min

data class CourseOrderingDto(
    @field:Min(1)
    val id: Long,
    val orderIndex: Int
)
