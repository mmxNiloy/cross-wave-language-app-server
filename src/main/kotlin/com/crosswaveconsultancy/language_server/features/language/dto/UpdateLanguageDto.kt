package com.crosswaveconsultancy.language_server.features.language.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateLanguageDto(
    @field:NotBlank
    val languageName: String?,
    val title: String?,
    val description: String?,
)
