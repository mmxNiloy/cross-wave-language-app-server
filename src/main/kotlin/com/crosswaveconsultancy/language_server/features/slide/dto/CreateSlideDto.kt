package com.crosswaveconsultancy.language_server.features.slide.dto

import com.crosswaveconsultancy.language_server.features.slide.document.SlideDocument
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateSlideDto(
    @field:NotBlank
    val title: String,

    @field:NotNull
    val lessonId: Long
) {
    fun toDocument(orderIndex: Int): SlideDocument {
        return SlideDocument(
            title = title,
            lessonId = lessonId,
            orderIndex = orderIndex
        )
    }
}
