package com.crosswaveconsultancy.language_server.features.slide.dto

import com.crosswaveconsultancy.language_server.features.slide.document.SlideDocument
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateSlideDto(
    val title: String? = null,

    @field:Valid
    val sections: List<CreateSectionDto>? = null,

    val orderIndex: Int? = null
)
