package com.crosswaveconsultancy.language_server.features.chapter.dto

import com.crosswaveconsultancy.language_server.features.chapter.ChapterEntity
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class CreateChapterDto(
    @field:NotBlank
    val title: String,
    val description: String?,
    @field:Min(1)
    val courseId: Long,
) {
    fun toEntity(): ChapterEntity {
        return ChapterEntity(
            title = title,
            description = description?:"",
            courseId = courseId,
            isActive = true,
        )
    }
}