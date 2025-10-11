package com.crosswaveconsultancy.language_server.features.slide.dto

data class ComponentResponseDto(
    val displayName: String,
    val props: Map<String, Any>,
    val children: List<ComponentResponseDto>? = null
)
