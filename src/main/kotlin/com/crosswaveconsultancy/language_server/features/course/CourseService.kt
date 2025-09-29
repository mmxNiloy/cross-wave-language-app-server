package com.crosswaveconsultancy.language_server.features.course

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.course.dto.CreateCourseDto
import com.crosswaveconsultancy.language_server.features.course.dto.UpdateCourseDto
import com.crosswaveconsultancy.language_server.features.module.ModuleRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.Optional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    fun getCourseById(id: Long): Optional<CourseEntity> {
        return courseRepository.findById(id)
    }

    fun getCourses(page: Int, limit: Int): List<CourseEntity> {
        var pageable: Pageable = PageRequest.of(page - 1, limit, Sort.by("orderIndex").ascending())

        return courseRepository.findAll(pageable).toList()
    }

    fun getCourseByModuleId(moduleId: Long, page: Int, limit: Int): List<CourseEntity> {
        var pageable: Pageable = PageRequest.of(page - 1, limit, Sort.by("orderIndex").ascending())
        return courseRepository.findByModuleId(moduleId, pageable).toList()
    }

    fun count(): Long {
        return courseRepository.count()
    }

    fun countByModuleId(moduleId: Long): Long {
        return courseRepository.countByModuleId(moduleId)
    }

    @Transactional
    fun save(course: CreateCourseDto): CourseEntity {
        val courseEntity = course.toEntity()

        // Check if a record with the given order index exists
        val hasConflict = courseRepository.existsByModuleIdAndOrderIndex(course.moduleId, course.orderIndex)
        if (hasConflict) {
            // if exists then update their order indices by incrementing 1
            courseRepository.shiftOrderIndexes(course.moduleId, course.orderIndex)
        }
        return courseRepository.save(courseEntity)

    }

    @Transactional
    fun update(id: Long, courseDto: UpdateCourseDto): CourseEntity {
        val courseEntity = courseRepository.findById(id).orElseThrow { ResourceNotFoundException("Course not found with id $id") }

        var course = courseEntity

        val moduleId = course.moduleId
        val orderIndex = courseDto.orderIndex

        val hasConflict = if (orderIndex != null && orderIndex != course.orderIndex) courseRepository.existsByModuleIdAndOrderIndex(moduleId, orderIndex) else false

        if (hasConflict && orderIndex != null) {
            courseRepository.shiftOrderIndexes(moduleId, orderIndex)
        }

        if(courseDto.title != null) course.title = courseDto.title
        if(courseDto.description != null) course.description = courseDto.description
        if(courseDto.orderIndex != null) course.orderIndex = courseDto.orderIndex

        return courseRepository.save(course)
    }

    @Transactional
    fun toggle(id: Long, isActive: Boolean): CourseEntity {
        val course = courseRepository.findById(id).orElseThrow { ResourceNotFoundException("Course not found with id $id") }
        course.isActive=isActive
        return courseRepository.save(course)
    }
}