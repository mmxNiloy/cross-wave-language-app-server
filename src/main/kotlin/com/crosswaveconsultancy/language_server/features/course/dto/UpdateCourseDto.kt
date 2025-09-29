package com.crosswaveconsultancy.language_server.features.course.dto

data class UpdateCourseDto(
    val title: String?,
    val description: String?,
    val orderIndex: Int?
)
