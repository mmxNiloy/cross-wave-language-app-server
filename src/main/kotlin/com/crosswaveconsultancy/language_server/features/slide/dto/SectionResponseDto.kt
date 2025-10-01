package com.crosswaveconsultancy.language_server.features.slide.dto

data class SectionResponseDto(
    val layout: String,
    val props: Map<String, Any>,
    val children: List<ComponentResponseDto>
)
