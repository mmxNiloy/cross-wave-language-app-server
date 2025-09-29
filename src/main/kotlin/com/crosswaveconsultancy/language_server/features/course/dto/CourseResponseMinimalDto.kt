package com.crosswaveconsultancy.language_server.features.course.dto

import com.crosswaveconsultancy.language_server.features.module.dto.ModuleResponseDto
import java.time.LocalDateTime

data class CourseResponseMinimalDto(
    val id: Long,
    val title: String,
    val description: String,
    val isActive: Boolean,
)
