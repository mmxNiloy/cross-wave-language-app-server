package com.crosswaveconsultancy.language_server.features.module.dto

data class CreateModuleDto(
    val title: String,
    val description: String,
    val languageId: Long,
)