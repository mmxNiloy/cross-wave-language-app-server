package com.crosswaveconsultancy.language_server.features.module.dto

import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import java.time.LocalDateTime

data class ModuleResponseDto(
    val id: Long,
    val title: String,
    val description: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val languageId: Long,
    val language: LanguageResponseDto?
)
