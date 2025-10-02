package com.crosswaveconsultancy.language_server.features.language.dto

import com.crosswaveconsultancy.language_server.features.language.LanguageEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateLanguageDto(
    @field:NotBlank
    val languageName: String,
    @field:NotBlank
    @field:Size(min = 2, max = 2)
    val shortCode: String,
    @field:NotBlank
    val title: String,
    val description: String?,
) {
    fun toEntity(): LanguageEntity {
        return LanguageEntity(
            languageName = languageName,
            shortCode = shortCode,
            title = title,
            description = description?:""
        )
    }
}