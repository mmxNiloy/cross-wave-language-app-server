package com.crosswaveconsultancy.language_server.features.slide

import com.crosswaveconsultancy.language_server.features.slide.dto.ComponentResponseDto
import com.crosswaveconsultancy.language_server.features.slide.dto.SectionResponseDto
import com.crosswaveconsultancy.language_server.features.slide.dto.SlideResponseDto
import org.assertj.core.api.Assertions.assertThat

fun SlideResponseDto.assertValidSlide() {
    assertThat(id).isNotNull.isNotEmpty
    assertThat(lessonId).isNotNull.isPositive
    assertThat(title).isNotNull
    assertThat(orderIndex).isNotNull.isPositive
    assertThat(isActive).isNotNull.isTrue
    assertThat(createdAt).isNotNull
    assertThat(updatedAt).isNotNull
    assertThat(sections).isNotNull
    sections.map {it.assertValidSection()}
}

fun SectionResponseDto.assertValidSection() {
    assertThat(layout).isNotNull
    assertThat(props).isNotNull
    assertThat(children).isNotNull
    children.map {it.assertValidComponent()}
}

fun ComponentResponseDto.assertValidComponent() {
    assertThat(type).isNotNull
    assertThat(props).isNotNull
}