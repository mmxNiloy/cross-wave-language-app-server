package com.crosswaveconsultancy.language_server.features.lesson.dto

import com.crosswaveconsultancy.language_server.features.lesson.LessonEntity
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class CreateLessonDto(
    @field:NotBlank
    val title: String,
    val description: String,
    @field:Min(1)
    val chapterId: Long,
    @field:Min(1)
    val orderIndex: Int,
) {
    fun toEntity(): LessonEntity {
        return LessonEntity(
            title = title,
            description = description,
            chapterId = chapterId,
            orderIndex = orderIndex,
        )
    }
}
