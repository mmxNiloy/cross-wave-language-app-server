package com.crosswaveconsultancy.language_server.features.slide.dto

import java.time.LocalDateTime

data class SlideResponseDto(
    val id: String,
    val lessonId: Long,
    val orderIndex: Int,
    val title: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val sections: List<SectionResponseDto>
)
