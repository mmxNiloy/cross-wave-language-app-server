package com.crosswaveconsultancy.language_server.features.userProgress.dto

import java.time.LocalDateTime

data class UserProgressDto(
    val id: String,
    val userId: String,
    val courseId: Long,
    val chapterId: Long,
    val lessonId: Long,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
