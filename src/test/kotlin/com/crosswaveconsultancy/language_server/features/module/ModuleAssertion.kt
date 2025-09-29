package com.crosswaveconsultancy.language_server.features.module

import com.crosswaveconsultancy.language_server.features.language.assertValidLanguage
import com.crosswaveconsultancy.language_server.features.module.dto.ModuleResponseDto
import org.assertj.core.api.Assertions.assertThat

fun ModuleResponseDto.assertValidModule() {
    assertThat(id).isNotNull()
    assertThat(title).isNotNull()
    assertThat(description).isNotNull()
    assertThat(isActive).isNotNull()
    assertThat(createdAt).isNotNull()
    assertThat(updatedAt).isNotNull()
    assertThat(languageId).isNotNull()
    language!!.assertValidLanguage()
}