package com.crosswaveconsultancy.language_server.features.slide.repository

import com.crosswaveconsultancy.language_server.features.slide.document.LessonCounterDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface LessonCounterRepository: MongoRepository<LessonCounterDocument, Long> {
}