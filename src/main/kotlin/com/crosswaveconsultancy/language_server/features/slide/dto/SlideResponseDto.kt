package com.crosswaveconsultancy.language_server.features.slide.dto

import java.time.LocalDateTime

open class SlideResponseDto(
    id: String,
    lessonId: Long,
    orderIndex: Int,
    title: String,
    section: SectionResponseDto,
    val isActive: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime? = LocalDateTime.now(),
    val previewImage: String?
): SlideResponseBaseDto(
    id,
    lessonId,
    orderIndex,
    title,
    section
)
