package com.crosswaveconsultancy.language_server.features.lesson.dto

data class UpdateLessonDto(
    val title: String?,
    val description: String?,
    val chapterId: Long?,
    val orderIndex: Int?
)
