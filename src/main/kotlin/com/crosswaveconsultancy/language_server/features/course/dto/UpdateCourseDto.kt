package com.crosswaveconsultancy.language_server.features.course.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class UpdateCourseDto(
    @field:NotBlank
    val title: String?,
    val description: String?,
    @field:Min(1)
    val orderIndex: Int?
)
