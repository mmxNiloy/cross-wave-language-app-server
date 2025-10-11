package com.crosswaveconsultancy.language_server.features.language

import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import org.assertj.core.api.Assertions.assertThat

fun LanguageResponseDto.assertValidModule() {
    assertThat(languageName).isNotNull()
    assertThat(title).isNotNull()
    assertThat(description).isNotNull()
    assertThat(isActive).isNotNull()
    assertThat(createdAt).isNotNull()
    assertThat(updatedAt).isNotNull()
}