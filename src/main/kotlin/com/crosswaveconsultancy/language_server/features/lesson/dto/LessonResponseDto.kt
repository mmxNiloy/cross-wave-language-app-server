package com.crosswaveconsultancy.language_server.features.lesson.dto

import com.crosswaveconsultancy.language_server.features.chapter.dto.ChapterResponseMinimalDto
import java.time.LocalDateTime

data class LessonResponseDto(
    val id: Long,
    var title: String,
    var description: String,
    var chapterId: Long,
    var orderIndex: Int,
    var isActive: Boolean = true,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
    val chapter: ChapterResponseMinimalDto?=null,
)