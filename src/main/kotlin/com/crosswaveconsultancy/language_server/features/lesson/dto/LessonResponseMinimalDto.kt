package com.crosswaveconsultancy.language_server.features.lesson.dto

data class LessonResponseMinimalDto(
    val id: Long,
    val title: String,
    val orderIndex: Int,
)
