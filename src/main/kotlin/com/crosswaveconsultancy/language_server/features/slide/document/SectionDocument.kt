package com.crosswaveconsultancy.language_server.features.slide.document

import com.crosswaveconsultancy.language_server.features.slide.dto.SectionResponseDto

data class SectionDocument(
    var layout: String, // "column", "grid", "row"
    var props: Map<String, Any> = emptyMap(), // layout-specific config
    var children: List<ComponentDocument> = emptyList()
)
