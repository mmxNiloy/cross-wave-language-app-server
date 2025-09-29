package com.crosswaveconsultancy.language_server.features.chapter.dto

import com.crosswaveconsultancy.language_server.features.course.dto.CourseResponseMinimalDto
import java.time.LocalDateTime

data class ChapterResponseMinimalDto(
    val id: Long,
    val title: String,
    val description: String,
)
