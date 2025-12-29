package com.crosswaveconsultancy.language_server.features.userProgress

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.lesson.LessonRepository
import com.crosswaveconsultancy.language_server.features.userProgress.dto.UserProgressDto
import com.crosswaveconsultancy.language_server.features.userProgress.dto.UserProgressFilterDto
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class UserProgressService(
    private val userProgressRepository: UserProgressRepository,
    private val lessonRepository: LessonRepository
) {
    fun getUserProgress(userId: String, filter: UserProgressFilterDto): List<UserProgressDto> {
        var progressList = userProgressRepository.findAllByUserId(
            userId,
            Sort.by("courseId").descending()
                .and(Sort.by("chapterId").descending())
                .and(Sort.by("lessonId").descending())
        )

        val lessonId = filter.lessonId
        val chapterId = filter.chapterId
        val courseId = filter.courseId

        if (lessonId != null) {
            progressList = progressList.filter { it.lessonId == lessonId }
        }

        if (chapterId != null) {
            progressList = progressList.filter { it.chapterId == chapterId }
        }

        if (courseId != null) {
            progressList = progressList.filter { it.courseId == courseId }
        }

        return progressList.map { it.toDto() }
    }

    fun createUserProgress(userId: String, lessonId: Long): UserProgressDto {
        val lesson = lessonRepository.findById(lessonId)
            .orElseThrow { ResourceNotFoundException("Lesson with id $lessonId not found!") }
        if (lesson.chapter == null) throw ResourceNotFoundException("This lesson is not assigned to a chapter.")

        val chapterId = lesson.chapterId
        val courseId = lesson.chapter!!.courseId

        val newUserProgress = UserProgressEntity(
            userId = userId,
            lessonId = lessonId,
            chapterId = chapterId,
            courseId = courseId
        )

        userProgressRepository.save(newUserProgress)

        return newUserProgress.toDto()
    }
}