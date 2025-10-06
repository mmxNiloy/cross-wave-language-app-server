package com.crosswaveconsultancy.language_server.features.course

import com.crosswaveconsultancy.language_server.features.course.dto.CourseResponseDto
import com.crosswaveconsultancy.language_server.features.course.dto.CreateCourseDto
import com.crosswaveconsultancy.language_server.features.course.dto.UpdateCourseDto
import com.crosswaveconsultancy.language_server.features.course.dto.SwapCourseOrderIndexDto
import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.util.PaginationRequestParamsDto
import com.crosswaveconsultancy.language_server.util.buildPaginationMetadata
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Course Management",
    description = "CRUD for courses. Each course is associated with a module. A course contains some chapters."
)
@RestController
@RequestMapping("/api/v1/course")
@Validated
class CourseController(
    private val courseService: CourseService
) {
    @GetMapping
    fun getCourses(
        @Valid @ParameterObject params: PaginationRequestParamsDto
    ): ApiResponsePaginated<CourseResponseDto> {
        val page = params.page ?: 1
        val limit = params.limit ?: 10

        val courses = courseService.getCourses(page, limit)
        val chapterCount = courseService.countChaptersByCourseIds(courses.map { it.id })
            .associate { (courseId, count) -> courseId to count }

        val payload = courses.map { it.toDto(chapterCount[it.id] ?: 0) }
        val totalCount = courseService.count()
        val paginationMetadata = buildPaginationMetadata(page, limit, payload.size, totalCount)
        return ApiResponsePaginated<CourseResponseDto>(
            ok = true,
            status = 200,
            message = "Courses fetched successfully",
            payload = payload,
            path = "/v1/course",
            paginationMetadata = paginationMetadata
        )
    }

    @GetMapping("/language/{languageCode}")
    fun getCourseByLanguageCode(
        @Valid @ParameterObject params: PaginationRequestParamsDto,
        @PathVariable @Size(min = 2, max = 2) languageCode: String
    ): ApiResponsePaginated<CourseResponseDto> {
        val page = params.page ?: 1
        val limit = params.limit ?: 10

        val courses = courseService.getCourseByLanguageCode(languageCode, page, limit)
        val chapterCount = courseService.countChaptersByCourseIds(courses.map { it.id })
            .associate { (courseId, count) -> courseId to count }

        val payload = courses.map { it.toDto(chapterCount[it.id] ?: 0) }
        val totalCount = courseService.countByLanguageCode(languageCode)
        val paginationMetadata = buildPaginationMetadata(page, limit, payload.size, totalCount)

        return ApiResponsePaginated(
            ok = true,
            status = 200,
            message = "Courses fetched successfully",
            payload = payload,
            path = "/v1/course/language/$languageCode",
            paginationMetadata = paginationMetadata
        )
    }

    @GetMapping("/{id}")
    fun getCourseById(@PathVariable @Min(1) id: Long): ApiResponse<CourseResponseDto> {
        val payload = courseService.getCourseById(id)
        return ApiResponse(
            status = 200,
            ok = true,
            message = "Course fetched successfully",
            payload = payload.toDtoWithChapters(),
            path = "/v1/course/$id"
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createCourse(@Valid @RequestBody courseDto: CreateCourseDto): ResponseEntity<ApiResponse<CourseResponseDto>> {
        val payload = courseService.save(courseDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse(
                status = 201,
                ok = true,
                message = "Course created successfully",
                payload = payload.toDto(),
                path = "/v1/course"
            )
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/restore/{id}")
    fun restoreCourse(@PathVariable id: Long): ApiResponse<CourseResponseDto> {
        val payload = courseService.toggle(id, true)
        return ApiResponse(
            status = 200,
            ok = true,
            message = "Course restored successfully",
            path = "/v1/course/$id",
            payload = payload.toDto()
        )
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/swap-order-index")
    fun swapOrderIndex(
        @Valid @RequestBody swapCourseOrderIndexDto: SwapCourseOrderIndexDto
    ): ApiResponse<List<CourseResponseDto>> {
        val payload = courseService.swapOrderIndex(swapCourseOrderIndexDto).map { it.toDto() }
        return ApiResponse(
            status = 200,
            ok = true,
            message = "Courses updated successfully",
            payload = payload,
            path = "/v1/course/swap-order-index"
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    fun updateCourse(
        @PathVariable id: Long,
        @Valid @RequestBody courseDto: UpdateCourseDto
    ): ApiResponse<CourseResponseDto> {
        val payload = courseService.update(id, courseDto)
        return ApiResponse(
            status = 200,
            ok = true,
            message = "Course updated successfully",
            payload = payload.toDto(),
            path = "/v1/course/$id"
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteCourse(@PathVariable id: Long): ResponseEntity<ApiResponse<String>> {
        courseService.toggle(id, false)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            ApiResponse(
                status = 204,
                ok = true,
                message = "Course deleted successfully",
                path = "/v1/course/$id",
                payload = "Course deleted successfully"
            )
        )
    }
}