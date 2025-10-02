package com.crosswaveconsultancy.language_server.features.language

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.chapter.ChapterRepository
import com.crosswaveconsultancy.language_server.features.course.CourseRepository
import com.crosswaveconsultancy.language_server.features.language.dto.CreateLanguageDto
import com.crosswaveconsultancy.language_server.features.language.dto.LanguageStatsDto
import com.crosswaveconsultancy.language_server.features.language.dto.UpdateLanguageDto
import com.crosswaveconsultancy.language_server.features.lesson.LessonRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class LanguageService(
    private val languageRepository: LanguageRepository,
    private val courseRepository: CourseRepository,
    private val chapterRepository: ChapterRepository,
    private val lessonRepository: LessonRepository,
) {
    fun getLanguageById(id: String): LanguageEntity {
        return languageRepository.findById(id).orElseThrow { ResourceNotFoundException("Language not found with id $id") }
    }

    fun getLanguages(page: Int, limit: Int): List<LanguageEntity> {
        var pageable: Pageable = PageRequest.of(page - 1, limit, Sort.by("updatedAt").descending())

        return languageRepository.findAll(pageable).toList()
    }

    fun count(): Long {
        return languageRepository.count()
    }

    fun save(languageDto: CreateLanguageDto): LanguageEntity {
        return languageRepository.save(languageDto.toEntity())
    }

    @Transactional
    fun update(shortCode: String, languageDto: UpdateLanguageDto): LanguageEntity {
        var language = getLanguageById(shortCode)

        languageDto.languageName?.let { language.languageName = it }
        languageDto.title?.let { language.title = it }
        languageDto.description?.let { language.description = it }

        return languageRepository.save(language)
    }

    @Transactional
    fun getLanguageStats(id: String): LanguageStatsDto {
        val totalCourses = courseRepository.countByLanguageCode(id)
        val totalChapters = chapterRepository.countByCourse_LanguageCode(id)
        val totalLessons = lessonRepository.countByChapter_Course_LanguageCode(id)
        val totalSlides = -1L

        return LanguageStatsDto(
            totalCourses = totalCourses,
            totalChapters = totalChapters,
            totalLessons = totalLessons,
            totalSlides = totalSlides
        )
    }

    @Transactional
    fun toggle(id: String, active: Boolean): LanguageEntity {
        var language = getLanguageById(id)
        language.isActive = active
        return languageRepository.save(language)
    }
}