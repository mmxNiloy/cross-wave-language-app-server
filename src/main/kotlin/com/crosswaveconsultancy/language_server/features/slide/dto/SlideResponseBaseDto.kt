package com.crosswaveconsultancy.language_server.features.slide.dto

import java.time.LocalDateTime

open class SlideResponseBaseDto(
    open val id: String,
    open val lessonId: Long,
    open val orderIndex: Int,
    open val title: String,
    open val section: SectionResponseDto,
)