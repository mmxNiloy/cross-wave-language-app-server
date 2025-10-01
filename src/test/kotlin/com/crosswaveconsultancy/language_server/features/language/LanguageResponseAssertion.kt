package com.crosswaveconsultancy.language_server.features.language

import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseMinimalDto
import org.assertj.core.api.Assertions.assertThat

fun LanguageResponseDto.assertValidLanguage() {
    assertThat(id).isNotNull().isPositive
    assertThat(createdAt).isNotNull()
    assertThat(updatedAt).isNotNull()
    assertThat(name).isNotNull()
    assertThat(shortCode).isNotNull().hasSize(2)
}

fun LanguageResponseMinimalDto.assertValidLanguage() {
    assertThat(name).isNotNull()
    assertThat(shortCode).isNotNull().hasSize(2)
}