package com.crosswaveconsultancy.language_server.features.userProgress.dto

import jakarta.validation.constraints.Min

data class UserProgressFilterDto(
    @field:Min(0)
    val lessonId: Long?,
    @field:Min(0)
    val chapterId: Long?,
    @field:Min(0)
    val courseId: Long?
)
