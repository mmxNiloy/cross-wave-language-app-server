package com.crosswaveconsultancy.language_server.features.slide.dto

import com.crosswaveconsultancy.language_server.features.slide.document.SlideDocument
import com.crosswaveconsultancy.language_server.features.slide.dto.editor.EditorNodeDto
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateSlideDto(
    val title: String? = null,

    @field:Valid
    val data: Map<String, EditorNodeDto>? = null,

    val previewImage: String? = null
)
