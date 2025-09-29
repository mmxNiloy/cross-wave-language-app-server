package com.crosswaveconsultancy.language_server.features.course.dto

data class CreateCourseDto(
    val moduleId: Long,
    val title: String,
    val description: String,
    val orderIndex: Int
)