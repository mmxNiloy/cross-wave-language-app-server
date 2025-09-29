package com.crosswaveconsultancy.language_server.features.chapter.dto

import com.crosswaveconsultancy.language_server.features.course.dto.CourseResponseMinimalDto
import java.time.LocalDateTime

data class ChapterResponseDto(
    val id: Long,
    val title: String,
    val description: String,
    val orderIndex: Int,
    val isActive: Boolean,
    val courseId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,

    val course: CourseResponseMinimalDto?=null
)
