package com.crosswaveconsultancy.language_server.features.slide.dto.editor

import com.crosswaveconsultancy.language_server.features.slide.dto.ComponentResponseDto
import com.crosswaveconsultancy.language_server.features.slide.dto.SectionResponseDto

data class EditorNodeDto(
    val type: EditorNodeType,
    val isCanvas: Boolean,
    val props: Map<String, Any>,
    val displayName: String,
    val custom: EditorNodeCustom?,
    val hidden: Boolean,
    val nodes: List<String>,
    val linkedNodes: Map<String, String>,
    val parent: String?
) {
    fun toComponentDto(dataMap: Map<String, EditorNodeDto>): ComponentResponseDto {
        var children: MutableList<ComponentResponseDto> = emptyList<ComponentResponseDto>().toMutableList()

        nodes.map { children.add(dataMap[it]?.toComponentDto(dataMap)?:ComponentResponseDto(displayName = "Unknown", props = emptyMap())) }
        linkedNodes.values.map { children.add(dataMap[it]?.toComponentDto(dataMap)?:ComponentResponseDto(displayName = "Unknown", props = emptyMap())) }

        return ComponentResponseDto(
            displayName = displayName,
            props = props,
            children = children
        )
    }

    fun toSectionDto(dataMap: Map<String, EditorNodeDto>): SectionResponseDto {
        return SectionResponseDto(
            displayName = displayName,
            props = props,
            components = nodes.map { dataMap[it]?.toComponentDto(dataMap)?:ComponentResponseDto(displayName = "Unknown", props = emptyMap()) }
        )
    }
}