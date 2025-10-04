package com.crosswaveconsultancy.language_server.features.course.dto

data class SwapCourseOrderIndexDto(
    val courseId1: Long,
    val courseId2: Long,
    val newOrderIndex1: Int?,
    val newOrderIndex2: Int?
)
