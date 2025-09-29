package com.crosswaveconsultancy.language_server.features.language.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateLanguageDto(
    @field:NotBlank
    val name: String?,
    @field:NotBlank
    @field:Size(min = 2, max = 2)
    val shortCode: String?,
)
