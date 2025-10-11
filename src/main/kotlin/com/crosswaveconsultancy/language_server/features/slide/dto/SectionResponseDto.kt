package com.crosswaveconsultancy.language_server.features.slide.dto

data class SectionResponseDto(
    val displayName: String,
    val components: List<ComponentResponseDto> = emptyList(),
    val props: Map<String, Any> = emptyMap()
)
