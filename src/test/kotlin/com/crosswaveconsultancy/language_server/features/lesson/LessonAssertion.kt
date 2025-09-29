package com.crosswaveconsultancy.language_server.features.lesson

import com.crosswaveconsultancy.language_server.features.chapter.assertValidChapter
import com.crosswaveconsultancy.language_server.features.lesson.dto.LessonResponseDto
import org.assertj.core.api.Assertions.assertThat

fun LessonResponseDto.assertValidLesson() {
    assertThat(id).isNotNull.isPositive
    assertThat(title).isNotNull
    assertThat(description).isNotNull
    assertThat(chapterId).isNotNull.isPositive
    assertThat(orderIndex).isNotNull.isPositive
    assertThat(isActive).isNotNull.isTrue
    assertThat(createdAt).isNotNull
    assertThat(updatedAt).isNotNull
    chapter?.assertValidChapter()
}