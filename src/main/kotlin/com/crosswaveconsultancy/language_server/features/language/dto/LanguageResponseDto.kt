package com.crosswaveconsultancy.language_server.features.language.dto

import java.time.LocalDateTime

data class LanguageResponseDto(
    val id: Long,
    val name: String,
    val shortCode: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isActive: Boolean,
)
