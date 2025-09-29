package com.crosswaveconsultancy.language_server.features.chapter.dto

import com.crosswaveconsultancy.language_server.features.chapter.ChapterEntity
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class CreateChapterDto(
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val description: String,
    @Min(1)
    val courseId: Long,
    @Min(1)
    val orderIndex: Int,
) {
    fun toEntity(): ChapterEntity {
        return ChapterEntity(
            title = title,
            description = description,
            courseId = courseId,
            orderIndex = orderIndex,
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }
}