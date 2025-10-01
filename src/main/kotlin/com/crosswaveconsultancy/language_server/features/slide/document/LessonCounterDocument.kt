package com.crosswaveconsultancy.language_server.features.slide.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="lesson_counter")
data class LessonCounterDocument(
    @Id
    val lessonId: Long,
    var count: Int = 0,
    var inactiveCount: Int = 0
)
