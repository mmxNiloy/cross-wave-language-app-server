package com.crosswaveconsultancy.language_server.features.language.dto

import com.crosswaveconsultancy.language_server.features.language.LanguageEntity

data class LanguageDTO(
    val name: String,
    val shortCode: String,
) {
    fun toEntity(): LanguageEntity {
        return LanguageEntity(
            name = name,
            shortCode = shortCode,
        )
    }
}