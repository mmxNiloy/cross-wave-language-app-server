package com.crosswaveconsultancy.language_server.features.module.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateModuleDto(
    @NotBlank
    val title: String,

    val description: String?="",

    @Min(1)
    val languageId: Long,
)