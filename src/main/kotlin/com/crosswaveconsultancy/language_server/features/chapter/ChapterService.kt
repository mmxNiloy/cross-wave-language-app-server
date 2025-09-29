package com.crosswaveconsultancy.language_server.features.chapter

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.chapter.dto.CreateChapterDto
import com.crosswaveconsultancy.language_server.features.chapter.dto.UpdateChapterDto
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ChapterService(
    private val chapterRepository: ChapterRepository
) {
    fun getChapters(page: Int, limit: Int): List<ChapterEntity> {
        val pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "orderIndex"))
        return chapterRepository.findAll(pageable).toList()
    }

    fun count(): Long {
        return chapterRepository.count()
    }

    fun countByCourseId(courseId: Long): Long {
        return chapterRepository.countByCourseId(courseId)
    }

    fun getChaptersByCourseId(courseId: Long, page: Int, limit: Int): List<ChapterEntity> {
        val pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "orderIndex"))
        return chapterRepository.findByCourseId(courseId, pageable).toList()
    }

    fun getChapterById(id: Long): ChapterEntity {
        return chapterRepository.findById(id)
            .orElseThrow { throw ResourceNotFoundException("Chapter not found with id $id") }
    }

    @Transactional
    fun save(chapterDto: CreateChapterDto): ChapterEntity {
        val chapter = chapterDto.toEntity()

        // Check if a record with the given order index exists
        val hasConflict = chapterRepository.existsByCourseIdAndOrderIndex(chapter.courseId, chapter.orderIndex)
        if (hasConflict) {
            // if exists then update their order indices by incrementing 1
            chapterRepository.shiftOrderIndexes(chapter.courseId, chapter.orderIndex)
        }

        return chapterRepository.save(chapterDto.toEntity())
    }

    @Transactional
    fun toggle(id: Long, isActive: Boolean): ChapterEntity {
        val chapter = getChapterById(id)
        chapter.isActive = isActive
        return chapterRepository.save(chapter)
    }

    @Transactional
    fun update(id: Long, chapterDto: UpdateChapterDto): ChapterEntity {
        val chapter = getChapterById(id)

        val courseId = chapter.courseId
        val orderIndex = chapterDto.orderIndex

        val hasConflict = if (orderIndex != null && orderIndex != chapter.orderIndex) chapterRepository.existsByCourseIdAndOrderIndex(courseId, orderIndex) else false
        if (hasConflict && orderIndex != null) {
            chapterRepository.shiftOrderIndexes(courseId, orderIndex)
        }

        if (chapterDto.title != null)
            chapter.title = chapterDto.title
        if (chapterDto.description != null)
            chapter.description = chapterDto.description
        if (chapterDto.courseId != null)
            chapter.courseId = chapterDto.courseId
        if (chapterDto.orderIndex != null)
            chapter.orderIndex = chapterDto.orderIndex

        return chapterRepository.save(chapter)
    }
}