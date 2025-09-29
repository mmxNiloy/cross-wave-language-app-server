package com.crosswaveconsultancy.language_server.features.chapter.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class UpdateChapterDto(
    @field:NotBlank
    val title: String?,
    val description: String?,
    @field:Min(1)
    val courseId: Long?,
    @field:Min(1)
    val orderIndex: Int?,
)