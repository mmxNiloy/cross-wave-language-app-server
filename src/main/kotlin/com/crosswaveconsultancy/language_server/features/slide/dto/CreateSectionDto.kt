package com.crosswaveconsultancy.language_server.features.slide.dto

import com.crosswaveconsultancy.language_server.features.slide.document.SectionDocument
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

data class CreateSectionDto(
    @field:NotBlank
    val layout: String,

    val props: Map<String, Any> = emptyMap(),

    @field:Valid
    val children: List<CreateComponentDto> = emptyList()
) {
    fun toDocument(): SectionDocument {
        return SectionDocument(
            layout = layout,
            props = props,
            children = children.map { it.toDocument() }
        )
    }
}
