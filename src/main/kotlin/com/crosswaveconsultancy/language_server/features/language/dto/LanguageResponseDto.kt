package com.crosswaveconsultancy.language_server.features.language.dto

import java.time.LocalDateTime

data class LanguageResponseDto(
    val languageName: String,
    val title: String,
    val description: String,
    val shortCode: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isActive: Boolean,
)
