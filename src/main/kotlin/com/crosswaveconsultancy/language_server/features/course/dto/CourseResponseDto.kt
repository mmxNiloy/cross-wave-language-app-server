package com.crosswaveconsultancy.language_server.features.course.dto

import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import java.time.LocalDateTime

data class CourseResponseDto(
    val id: Long,
    val title: String,
    val description: String,
    val orderIndex: Int,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val languageCode: String,
    val language: LanguageResponseDto?
)
