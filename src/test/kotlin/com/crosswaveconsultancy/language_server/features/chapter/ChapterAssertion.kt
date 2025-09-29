package com.crosswaveconsultancy.language_server.features.chapter

import com.crosswaveconsultancy.language_server.features.chapter.dto.ChapterResponseDto
import com.crosswaveconsultancy.language_server.features.chapter.dto.ChapterResponseMinimalDto
import com.crosswaveconsultancy.language_server.features.course.assertValidCourse
import org.assertj.core.api.Assertions.assertThat

fun ChapterResponseDto.assertValidChapter() {
    assertThat(id).isNotNull.isPositive
    assertThat(title).isNotNull
    assertThat(description).isNotNull
    assertThat(courseId).isNotNull.isPositive
    assertThat(orderIndex).isNotNull.isPositive
    assertThat(isActive).isNotNull.isTrue
    assertThat(createdAt).isNotNull
    assertThat(updatedAt).isNotNull
    assertThat(course).isNotNull
    course?.assertValidCourse()
}

fun ChapterResponseMinimalDto.assertValidChapter() {
    assertThat(id).isNotNull.isPositive
    assertThat(title).isNotNull
    assertThat(description).isNotNull
}