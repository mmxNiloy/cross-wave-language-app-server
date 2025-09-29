package com.crosswaveconsultancy.language_server.features.language.dto

import com.crosswaveconsultancy.language_server.features.language.LanguageEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateLanguageDto(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    @field:Size(min = 2, max = 2)
    val shortCode: String,
) {
    fun toEntity(): LanguageEntity {
        return LanguageEntity(
            name = name,
            shortCode = shortCode,
        )
    }
}