package com.crosswaveconsultancy.language_server.features.slide.repository

import com.crosswaveconsultancy.language_server.features.slide.document.SlideDocument
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface SlideRepository: MongoRepository<SlideDocument, String> {
    fun findByIsActive(pageable: Pageable, isActive: Boolean=true): Page<SlideDocument>
    fun countByIsActive(isActive: Boolean=true): Long

    fun findByLessonIdAndIsActive(pageable: Pageable, lessonId: Long, isActive: Boolean=true): Page<SlideDocument>
    fun countByLessonIdAndIsActive(lessonId: Long, isActive: Boolean=true): Long

    fun findByIdAndIsActive(id: String, isActive: Boolean=true): Optional<SlideDocument>
}