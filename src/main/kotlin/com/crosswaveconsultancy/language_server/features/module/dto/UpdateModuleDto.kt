package com.crosswaveconsultancy.language_server.features.module.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class UpdateModuleDto(
    @field:NotBlank
    val title: String?,
    val description: String?,
    @field:Min(1)
    val languageId: Long?,
)
