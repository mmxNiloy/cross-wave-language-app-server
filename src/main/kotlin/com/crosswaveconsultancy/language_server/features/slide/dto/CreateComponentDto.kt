package com.crosswaveconsultancy.language_server.features.slide.dto

import com.crosswaveconsultancy.language_server.features.slide.document.ComponentDocument
import jakarta.validation.constraints.NotBlank

data class CreateComponentDto(
    @field:NotBlank
    val type: String,

    val props: Map<String, Any> = emptyMap()
) {
    fun toDocument(): ComponentDocument {
        return ComponentDocument(
            type = type,
            props = props
        )
    }
}
