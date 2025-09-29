package com.crosswaveconsultancy.language_server.features.module.dto

import com.crosswaveconsultancy.language_server.features.module.ModuleEntity
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateModuleDto(
    @field:NotBlank
    val title: String,

    val description: String?,

    @field:Min(1)
    val languageId: Long,
) {
    fun toEntity(): ModuleEntity {
        return ModuleEntity(
            title = title,
            description = description?:"",
            languageId = languageId,
        )
    }
}