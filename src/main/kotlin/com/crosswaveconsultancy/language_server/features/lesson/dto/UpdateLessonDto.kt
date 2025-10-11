package com.crosswaveconsultancy.language_server.features.lesson.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class UpdateLessonDto(
    @field:NotBlank
    val title: String?,
    val description: String?,
    @field:Min(1)
    val chapterId: Long?
)
