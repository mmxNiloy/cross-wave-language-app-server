package com.crosswaveconsultancy.language_server.features.course.dto

data class CourseResponseMinimalDto(
    val id: Long,
    val title: String,
    val description: String,
    val isActive: Boolean,
)
