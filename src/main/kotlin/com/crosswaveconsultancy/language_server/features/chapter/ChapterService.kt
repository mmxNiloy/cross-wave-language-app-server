package com.crosswaveconsultancy.language_server.features.chapter

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.chapter.dto.CreateChapterDto
import com.crosswaveconsultancy.language_server.features.chapter.dto.SwapChapterOrderIndexDto
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
        val pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "orderIndex"))
        return chapterRepository.findAll(pageable).toList()
    }

    fun count(): Long {
        return chapterRepository.count()
    }

    fun countByCourseId(courseId: Long): Long {
        return chapterRepository.countByCourseId(courseId)
    }

    fun countLessonsByChapterIds(chapterIds: List<Long>): List<Array<Long>> {
        return chapterRepository.countLessonsByChapterIds(chapterIds)
    }

    fun getChaptersByCourseId(courseId: Long, page: Int, limit: Int): List<ChapterEntity> {
        val pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "orderIndex"))
        return chapterRepository.findByCourseId(courseId, pageable).toList()
    }

    fun getChapterById(id: Long): ChapterEntity {
        return chapterRepository.findById(id)
            .orElseThrow { throw ResourceNotFoundException("Chapter not found with id $id") }
    }

    @Transactional
    fun save(chapterDto: CreateChapterDto): ChapterEntity {
        val chapter = chapterDto.toEntity()

        val newOrderIndex = countByCourseId(chapter.courseId)
        chapter.orderIndex = newOrderIndex.toInt()

        return chapterRepository.save(chapter)
    }

    @Transactional
    fun swapOrderIndex(data: SwapChapterOrderIndexDto): List<ChapterEntity> {
        val chapters = chapterRepository.findAllById(listOf(data.chapterId1, data.chapterId2))
        if(chapters.size != 2) throw ResourceNotFoundException("Chapters not found with ids ${data.chapterId1} or ${data.chapterId2}")

        val chapter1 = chapters[0]
        val chapter2 = chapters[1]

        val chapter1OrderIndex = chapter1.orderIndex
        val chapter2OrderIndex = chapter2.orderIndex

        // Temporarily change the order index of course 2
        chapter2.orderIndex = -1
        chapter1.orderIndex = -2
        chapterRepository.saveAll(listOf(chapter1, chapter2))
        chapterRepository.flush()

        // Swap the values now, bypasses unique constraint. That's why a hoola-hoop of temporary value
        chapter1.orderIndex = chapter2OrderIndex
        chapter2.orderIndex = chapter1OrderIndex
        return chapterRepository.saveAll(listOf(chapter1, chapter2))
    }

    fun delete(id: Long) {
        return chapterRepository.deleteById(id)
    }

    @Transactional
    fun update(id: Long, chapterDto: UpdateChapterDto): ChapterEntity {
        val chapter = getChapterById(id)

        val courseId = chapter.courseId
        val orderIndex = chapterDto.orderIndex

        val hasConflict =
            if (orderIndex != null && orderIndex != chapter.orderIndex) chapterRepository.existsByCourseIdAndOrderIndex(
                courseId,
                orderIndex
            ) else false
        if (hasConflict && orderIndex != null) {
            if (orderIndex < chapter.orderIndex)
                chapterRepository.shiftUpIndexes(courseId, orderIndex, chapter.orderIndex)
            else chapterRepository.shiftDownIndexes(courseId, orderIndex, chapter.orderIndex)
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