package com.crosswaveconsultancy.language_server.features.course

import com.crosswaveconsultancy.language_server.features.course.dto.CourseResponseDto
import com.crosswaveconsultancy.language_server.features.course.dto.CourseResponseMinimalDto
import com.crosswaveconsultancy.language_server.features.module.assertValidModule
import org.assertj.core.api.Assertions.assertThat

fun CourseResponseDto.assertValidCourse() {
    assertThat(id).isNotNull()
    assertThat(title).isNotNull()
    assertThat(description).isNotNull()
    assertThat(orderIndex).isNotNull()
    assertThat(isActive).isNotNull()
    assertThat(createdAt).isNotNull()
    assertThat(updatedAt).isNotNull()
    assertThat(moduleId).isNotNull()
    module!!.assertValidModule()
}

fun CourseResponseMinimalDto.assertValidCourse() {
    assertThat(id).isNotNull()
    assertThat(title).isNotNull()
    assertThat(description).isNotNull()
    assertThat(isActive).isNotNull()
}