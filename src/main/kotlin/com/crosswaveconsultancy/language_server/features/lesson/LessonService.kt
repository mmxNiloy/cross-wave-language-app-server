package com.crosswaveconsultancy.language_server.features.lesson

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.lesson.dto.SwapLessonOrderIndexDto
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

        val newOrderIndex = countByChapterId(lessonDto.chapterId)
        lesson.orderIndex = newOrderIndex.toInt()

        return lessonRepository.save(lesson)
    }

    @Transactional
    fun update(id: Long, lessonDto: UpdateLessonDto): LessonEntity {
        val lesson = getLessonById(id)

        if (lessonDto.title != null)
            lesson.title = lessonDto.title
        if (lessonDto.description != null)
            lesson.description = lessonDto.description
        if (lessonDto.chapterId != null)
            lesson.chapterId = lessonDto.chapterId

        return lessonRepository.save(lesson)
    }

    @Transactional
    fun swapOrderIndex(data: SwapLessonOrderIndexDto): List<LessonEntity> {
        val lessons = lessonRepository.findAllById(listOf(data.lessonId1, data.lessonId2))
        if(lessons.size != 2) throw ResourceNotFoundException("Courses not found with ids ${data.lessonId1} or ${data.lessonId2}")

        val lesson1 = lessons[0]
        val lesson2 = lessons[1]

        val lesson1OrderIndex = lesson1.orderIndex
        val lesson2OrderIndex = lesson2.orderIndex

        // Temporarily change the order index of course 2
        lesson2.orderIndex = -1
        lesson1.orderIndex = -2
        lessonRepository.saveAll(listOf(lesson1, lesson2))
        lessonRepository.flush()

        // Swap the values now, bypasses unique constraint. That's why a hoola-hoop of temporary value
        lesson1.orderIndex = lesson2OrderIndex
        lesson2.orderIndex = lesson1OrderIndex
        return lessonRepository.saveAll(listOf(lesson1, lesson2))
    }

    fun delete(id: Long) {
        return lessonRepository.deleteById(id)
    }
}