package com.crosswaveconsultancy.language_server.features.module

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.chapter.ChapterRepository
import com.crosswaveconsultancy.language_server.features.course.CourseRepository
import com.crosswaveconsultancy.language_server.features.language.LanguageEntity
import com.crosswaveconsultancy.language_server.features.language.LanguageRepository
import com.crosswaveconsultancy.language_server.features.lesson.LessonRepository
import com.crosswaveconsultancy.language_server.features.module.dto.CreateModuleDto
import com.crosswaveconsultancy.language_server.features.module.dto.ModuleStatsDto
import com.crosswaveconsultancy.language_server.features.module.dto.UpdateModuleDto
import com.crosswaveconsultancy.language_server.features.slide.repository.SlideRepository
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class ModuleService(
    private val moduleRepository: ModuleRepository,
    private val courseRepository: CourseRepository,
    private val chapterRepository: ChapterRepository,
    private val lessonRepository: LessonRepository,
    private val slideRepository: SlideRepository
) {
    fun findById(id: Long): ModuleEntity {
        return moduleRepository.findById(id).orElseThrow { ResourceNotFoundException("Module not found with id $id") }
    }

    fun findAll(page: Int, size: Int): List<ModuleEntity> {
        var pageRequest = PageRequest.of(page - 1, size)
        return moduleRepository.findAll(pageRequest).toList()
    }

    fun save(createModuleDto: CreateModuleDto): ModuleEntity {
        val moduleEntity = createModuleDto.toEntity()
        return moduleRepository.save(moduleEntity)
    }

    fun deleteById(id: Long) {
        moduleRepository.deleteById(id)
    }

    fun count(): Long {
        return moduleRepository.count()
    }

    fun update(id: Long, updateModuleDto: UpdateModuleDto): ModuleEntity {
        val moduleEntity = findById(id)

        if(updateModuleDto.title != null)
        moduleEntity.title = updateModuleDto.title
        if(updateModuleDto.description != null)
        moduleEntity.description = updateModuleDto.description
        if(updateModuleDto.languageId != null)
        moduleEntity.languageId = updateModuleDto.languageId

        return moduleRepository.save(moduleEntity)
    }

    @Transactional
    fun toggle(id: Long, isActive: Boolean): ModuleEntity {
        val moduleEntity = findById(id)
        moduleEntity.isActive = isActive
        return moduleRepository.save(moduleEntity)
    }

    @Transactional
    fun getModuleStats(id: Long): ModuleStatsDto {
        val totalCourses = courseRepository.countByModuleId(id)
        val totalChapters = chapterRepository.countByCourse_ModuleId(id)
        val totalLessons = lessonRepository.countByChapter_Course_ModuleId(id)
        val totalSlides = -1L

        return ModuleStatsDto(
            totalCourses = totalCourses,
            totalChapters = totalChapters,
            totalLessons = totalLessons,
            totalSlides = totalSlides
        )
    }
}