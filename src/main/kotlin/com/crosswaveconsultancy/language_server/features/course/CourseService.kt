package com.crosswaveconsultancy.language_server.features.course

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.course.dto.CourseOrderingDto
import com.crosswaveconsultancy.language_server.features.course.dto.CreateCourseDto
import com.crosswaveconsultancy.language_server.features.course.dto.SwapCourseOrderIndexDto
import com.crosswaveconsultancy.language_server.features.course.dto.UpdateCourseDto
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    fun getCourseById(id: Long): CourseEntity {
        return courseRepository.findById(id).orElseThrow { ResourceNotFoundException("Course not found with id $id") }
    }

    fun getCourses(page: Int, limit: Int): List<CourseEntity> {
        var pageable: Pageable = PageRequest.of(page - 1, limit, Sort.by("orderIndex").ascending())

        return courseRepository.findAll(pageable).toList()
    }

    fun getCourseByLanguageCode(languageCode: String, page: Int, limit: Int): List<CourseEntity> {
        var pageable: Pageable = PageRequest.of(page - 1, limit, Sort.by("orderIndex").ascending())
        return courseRepository.findByLanguageCode(languageCode, pageable).toList()
    }

    fun count(): Long {
        return courseRepository.count()
    }

    fun countByLanguageCode(languageCode: String): Long {
        return courseRepository.countByLanguageCode(languageCode)
    }

    fun countChaptersByCourseId(courseId: Long): Long {
        return courseRepository.countChaptersByCourseId(courseId)
    }

    fun countChaptersByCourseIds(courseIds: List<Long>): List<Array<Long>> {
        return courseRepository.countChaptersByCourseIds(courseIds)
    }

    @Transactional
    fun save(course: CreateCourseDto): CourseEntity {
        var courseEntity = course.toEntity()

        // Check if a record with the given order index exists
        val newOrderIndex = countByLanguageCode(course.languageCode)
        courseEntity.orderIndex = newOrderIndex.toInt()
        return courseRepository.save(courseEntity)

    }

    @Transactional
    fun update(id: Long, courseDto: UpdateCourseDto): CourseEntity {
        val courseEntity =
            courseRepository.findById(id).orElseThrow { ResourceNotFoundException("Course not found with id $id") }

        var course = courseEntity

        if (courseDto.title != null) course.title = courseDto.title
        if (courseDto.description != null) course.description = courseDto.description
        if (courseDto.orderIndex != null) course.orderIndex = courseDto.orderIndex

        return courseRepository.save(course)
    }

    @Transactional
    fun swapOrderIndex(data: SwapCourseOrderIndexDto): List<CourseEntity> {
        val courses = courseRepository.findAllById(listOf(data.courseId1, data.courseId2))
        if(courses.size != 2) throw ResourceNotFoundException("Courses not found with ids ${data.courseId1} or ${data.courseId2}")

        val course1 = courses[0]
        val course2 = courses[1]

        val course1OrderIndex = course1.orderIndex
        val course2OrderIndex = course2.orderIndex

        // Temporarily change the order index of course 2
        course2.orderIndex = -1
        course1.orderIndex = -2
        courseRepository.saveAll(listOf(course1, course2))
        courseRepository.flush()

        // Swap the values now, bypasses unique constraint. That's why a hoola-hoop of temporary value
        course1.orderIndex = course2OrderIndex
        course2.orderIndex = course1OrderIndex
        return courseRepository.saveAll(listOf(course1, course2))
    }

    @Transactional
    fun toggle(id: Long, isActive: Boolean): CourseEntity {
        val course =
            courseRepository.findById(id).orElseThrow { ResourceNotFoundException("Course not found with id $id") }
        course.isActive = isActive
        return courseRepository.save(course)
    }
}