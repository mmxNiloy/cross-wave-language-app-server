package com.crosswaveconsultancy.language_server.features.chapter.dto

data class UpdateChapterDto(
    val title: String?,
    val description: String?,
    val courseId: Long?,
    val orderIndex: Int?,
)