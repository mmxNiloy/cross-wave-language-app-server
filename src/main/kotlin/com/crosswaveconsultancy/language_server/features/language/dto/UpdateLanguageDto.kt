package com.crosswaveconsultancy.language_server.features.language.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateLanguageDto(
    @NotBlank
    val name: String?,
    @NotBlank
    @Size(min = 2, max = 2)
    val shortCode: String?,
)
