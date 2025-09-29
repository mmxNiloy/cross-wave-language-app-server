package com.crosswaveconsultancy.language_server.features.module.dto

data class UpdateModuleDto(
    val id: Long,
    val title: String,
    val description: String,
    val languageId: Long,
)
