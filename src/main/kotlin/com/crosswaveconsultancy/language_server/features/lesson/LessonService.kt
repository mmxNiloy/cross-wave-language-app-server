package com.crosswaveconsultancy.language_server.features.lesson

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.lesson.dto.CreateLessonDto
import com.crosswaveconsultancy.language_server.features.lesson.dto.UpdateLessonDto
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class LessonService(
    private val lessonRepository: LessonRepository
) {
    fun getLessons(page: Int, limit: Int): List<LessonEntity> {
        val pageable: Pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "orderIndex"))
        return lessonRepository.findAll(pageable).toList()
    }

    fun count(): Long {
        return lessonRepository.count()
    }

    fun getLessonsByChapterId(chapterId: Long, page: Int, limit: Int): List<LessonEntity> {
        val pageable: Pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "orderIndex"))
        return lessonRepository.findByChapterId(chapterId, pageable).toList()
    }

    fun countByChapterId(chapterId: Long): Long {
        return lessonRepository.countByChapterId(chapterId)
    }

    fun getLessonById(id: Long): LessonEntity {
        return lessonRepository.findById(id).orElseThrow {
            throw ResourceNotFoundException("Lesson not found with id $id")
        }
    }

    @Transactional
    fun save(lessonDto: CreateLessonDto): LessonEntity {
        val lesson = lessonDto.toEntity()

        // Check if a record with the given order index exists
        val hasConflict = lessonRepository.existsByChapterIdAndOrderIndex(lesson.chapterId, lesson.orderIndex)
        if (hasConflict) {
            // if exists then update their order indices by incrementing 1
            lessonRepository.shiftOrderIndexes(lesson.chapterId, lesson.orderIndex)
        }

        return lessonRepository.save(lesson)
    }

    @Transactional
    fun update(id: Long, lessonDto: UpdateLessonDto): LessonEntity {
        val lesson = getLessonById(id)

        val chapterId = lesson.chapterId
        val orderIndex = lessonDto.orderIndex

        val hasConflict =
            if (orderIndex != null && orderIndex != lesson.orderIndex) lessonRepository.existsByChapterIdAndOrderIndex(
                chapterId,
                orderIndex
            ) else false
        if (hasConflict && orderIndex != null) {
            if (orderIndex < lesson.orderIndex)
                lessonRepository.shiftUpIndexes(chapterId, orderIndex, lesson.orderIndex)
            else lessonRepository.shiftDownIndexes(chapterId, orderIndex, lesson.orderIndex)
        }

        if (lessonDto.title != null)
            lesson.title = lessonDto.title
        if (lessonDto.description != null)
            lesson.description = lessonDto.description
        if (lessonDto.chapterId != null)
            lesson.chapterId = lessonDto.chapterId
        if (lessonDto.orderIndex != null)
            lesson.orderIndex = lessonDto.orderIndex

        return lessonRepository.save(lesson)
    }

    @Transactional
    fun toggle(id: Long, isActive: Boolean): LessonEntity {
        val lesson = getLessonById(id)
        lesson.isActive = isActive
        return lessonRepository.save(lesson)
    }
}