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
    fun toComponentDto(dataMap: Map<String, EditorNodeDto>, shouldTrimProps: Boolean? = false): ComponentResponseDto {
        var children: MutableList<ComponentResponseDto> = emptyList<ComponentResponseDto>().toMutableList()

        nodes.map { children.add(dataMap[it]?.toComponentDto(dataMap, shouldTrimProps)?:ComponentResponseDto(displayName = "Unknown", props = emptyMap())) }
        linkedNodes.values.map { children.add(dataMap[it]?.toComponentDto(dataMap, shouldTrimProps)?:ComponentResponseDto(displayName = "Unknown", props = emptyMap())) }

        val properties = if(shouldTrimProps == true) trimProps() else props

        return ComponentResponseDto(
            displayName = displayName,
            props = properties,
            children = children
        )
    }

    private fun extractProps(propKeys: List<String>): Map<String, Any> {
        val extractedProps: MutableMap<String, Any> = HashMap<String, Any>()
        for(key in propKeys) {
            if(props.contains(key)) {
                extractedProps[key] = props[key]!!
            }
        }
        return extractedProps
    }

    private fun trimButtonProps(): Map<String, Any> {
        val propKeys = listOf("buttonStyle", "text", "textComponent", "action")
        return extractProps(propKeys)
    }

    private fun trimContainerProps(): Map<String, Any> {
        return props
    }
    private fun trimFillInTheBlanksProps(): Map<String, Any> {
        val propKeys = listOf("textSegments", "blanks", "hint")
        return extractProps(propKeys)
    }
    private fun trimImageProps(): Map<String, Any> {
        return props
    }
    private fun trimMatchingProps(): Map<String, Any> {
        val propKeys = listOf("question", "pairs", "hint")
        return extractProps(propKeys)
    }
    private fun trimMcqProps(): Map<String, Any> {
        val propKeys = listOf("question", "options", "correctAnswerId", "hint")
        return extractProps(propKeys)
    }
    private fun trimRearrangeProps(): Map<String, Any> {
        val propKeys = listOf("question", "items", "wrongItems", "hint")
        return extractProps(propKeys)
    }
    private fun trimTextProps(): Map<String, Any> {
        val propKeys = listOf("text", "type", "leadingIcon", "trailingIcon")
        return extractProps(propKeys)
    }

    private fun trimProps(): Map<String, Any> {
        when(displayName) {
            "Button" -> return trimButtonProps()
            "Container" -> return trimContainerProps()
            "FillInTheBlanks" -> return trimFillInTheBlanksProps()
            "Image" -> return trimImageProps()
            "Matching" -> return trimMatchingProps()
            "MCQ" -> return trimMcqProps()
            "RearrangeTool" -> return trimRearrangeProps()
            "Text" -> return trimTextProps()
            else -> return props
        }
    }

    fun toSectionDto(dataMap: Map<String, EditorNodeDto>, shouldTrimProps: Boolean? = false): SectionResponseDto {
        return SectionResponseDto(
            displayName = displayName,
            props = props,
            components = nodes.map { dataMap[it]?.toComponentDto(dataMap, shouldTrimProps)?:ComponentResponseDto(displayName = "Unknown", props = emptyMap()) }
        )
    }
}