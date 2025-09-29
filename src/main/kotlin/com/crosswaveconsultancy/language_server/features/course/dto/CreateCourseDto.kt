package com.crosswaveconsultancy.language_server.features.course.dto

import com.crosswaveconsultancy.language_server.features.course.CourseEntity
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateCourseDto(
    @field:Min(1)
    val moduleId: Long,
    @field:NotBlank
    val title: String,
    val description: String?,
    @field:Min(1)
    val orderIndex: Int
) {
    fun toEntity(): CourseEntity {
        return CourseEntity(
            title = title,
            description = description ?: "",
            orderIndex = orderIndex,
            moduleId = moduleId,
        )
    }
}