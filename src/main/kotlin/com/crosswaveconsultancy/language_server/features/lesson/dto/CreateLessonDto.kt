package com.crosswaveconsultancy.language_server.features.lesson.dto

import com.crosswaveconsultancy.language_server.features.lesson.LessonEntity
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateLessonDto(
    @field:NotBlank
    val title: String,
    val description: String,
    @field:Min(1)
    val chapterId: Long,
) {
    fun toEntity(): LessonEntity {
        return LessonEntity(
            title = title,
            description = description,
            chapterId = chapterId,
        )
    }
}
