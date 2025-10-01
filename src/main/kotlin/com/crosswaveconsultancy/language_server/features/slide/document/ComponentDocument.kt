package com.crosswaveconsultancy.language_server.features.slide.document

import com.crosswaveconsultancy.language_server.features.slide.dto.ComponentResponseDto

data class ComponentDocument(
    var type: String, // "text", "mcq", "button-primary", etc.
    var props: Map<String, Any> = emptyMap()
) {
    fun toDto(): ComponentResponseDto {
        return ComponentResponseDto(
            type = type,
            props = props
        )
    }
}
