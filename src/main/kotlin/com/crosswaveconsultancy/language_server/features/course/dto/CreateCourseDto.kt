package com.crosswaveconsultancy.language_server.features.course.dto

import com.crosswaveconsultancy.language_server.features.course.CourseEntity
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCourseDto(
    @field:NotBlank
    @field:Size(min = 2, max = 2)
    val languageCode: String,
    @field:NotBlank
    val title: String,
    val description: String?
) {
    fun toEntity(): CourseEntity {
        return CourseEntity(
            title = title,
            description = description ?: "",
            languageCode = languageCode,
        )
    }
}