package com.crosswaveconsultancy.language_server.features.lesson

import com.crosswaveconsultancy.language_server.features.lesson.dto.CreateLessonDto
import com.crosswaveconsultancy.language_server.features.lesson.dto.LessonResponseDto
import com.crosswaveconsultancy.language_server.features.lesson.dto.SwapLessonOrderIndexDto
import com.crosswaveconsultancy.language_server.features.lesson.dto.UpdateLessonDto
import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.util.PaginationRequestParamsDto
import com.crosswaveconsultancy.language_server.util.buildPaginationMetadata
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Lesson Management", description = "CRUD for lessons. Each lesson is associated with a chapter. Each lesson contains some slides.")
@RestController
@RequestMapping("/api/v1/lesson")
@Validated
class LessonController(
    private val lessonService: LessonService
) {
    @GetMapping
    fun getAllLessons(
        @Valid @ParameterObject params: PaginationRequestParamsDto
    ): ApiResponsePaginated<LessonResponseDto> {
        val page = params.page ?: 1
        val limit = params.limit ?: 10
        val payload = lessonService.getLessons(page, limit).map { it.toDto() }
        val count = lessonService.count()
        val paginationMetadata = buildPaginationMetadata(page, limit, payload.size, count)

        return ApiResponsePaginated(
            ok = true,
            status = 200,
            message = "Lessons fetched successfully",
            payload = payload,
            path = "/v1/lesson",
            paginationMetadata = paginationMetadata
        )
    }

    @GetMapping("/chapter/{id}")
    fun getLessonsByChapterId(
        @Valid @ParameterObject params: PaginationRequestParamsDto,
        @PathVariable @Min(1) id: Long
    ): ApiResponsePaginated<LessonResponseDto> {
        val page = params.page?:1
        val limit = params.limit?:10
        val payload = lessonService.getLessonsByChapterId(id, page, limit).map { it.toDto() }
        val count = lessonService.countByChapterId(id)
        val paginationMetadata = buildPaginationMetadata(page, limit, payload.size, count)

        return ApiResponsePaginated(
            ok = true,
            status = 200,
            message = "Lessons fetched successfully",
            payload = payload,
            path = "/v1/lesson/chapter/$id",
            paginationMetadata = paginationMetadata
        )
    }

    @GetMapping("/{id}")
    fun getLessonById(
        @PathVariable @Min(1) id: Long
    ): ApiResponse<LessonResponseDto> {
        val payload = lessonService.getLessonById(id).toDto()

        return ApiResponse(
            ok = true,
            status = 200,
            message = "Lesson fetched successfully",
            payload = payload,
            path = "/v1/lesson/$id"
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createLesson(
        @Valid @RequestBody lessonRequestDto: CreateLessonDto
    ): ResponseEntity<ApiResponse<LessonResponseDto>> {
        val payload = lessonService.save(lessonRequestDto)

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse(
                ok = true,
                status = 201,
                message = "Lesson created successfully",
                payload = payload.toDto(),
                path = "/v1/lesson"
            )
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/swap-order-index")
    fun swapOrderIndex(
        @Valid @RequestBody swapLessonOrderIndexDto: SwapLessonOrderIndexDto
    ): ApiResponse<List<LessonResponseDto>> {
        var payload = lessonService.swapOrderIndex(swapLessonOrderIndexDto).map { it.toDto() }

        return ApiResponse<List<LessonResponseDto>>(
                status = 200,
                ok = true,
                message = "Lesson order index swapped successfully",
                payload = payload,
                path = "/v1/lesson/swap-order-index"
            )

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/restore/{id}")
    fun restoreLesson(
        @PathVariable @Min(1) id: Long
    ): ApiResponse<LessonResponseDto> {
        val payload = lessonService.toggle(id, true)

        return ApiResponse(
            ok = true,
            status = 200,
            message = "Lesson restored successfully",
            payload = payload.toDto(),
            path = "/v1/lesson/$id"
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    fun updateLesson(
        @Valid @RequestBody lessonUpdateDto: UpdateLessonDto,
        @PathVariable @Min(1) id: Long
    ): ApiResponse<LessonResponseDto> {
        val payload = lessonService.update(id, lessonUpdateDto)

        return ApiResponse(
                ok = true,
                status = 200,
                message = "Lesson updated successfully",
                payload = payload.toDto(),
                path = "/v1/lesson/$id"
            )

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteLesson(
        @PathVariable @Min(1) id: Long
    ): ResponseEntity<ApiResponse<String>> {
        val payload = lessonService.toggle(id, false)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse(
            ok = true,
            status = 204,
            message = "Lesson deleted successfully",
            payload = "Lesson deleted successfully",
            path = "/v1/lesson/$id"
        ))
    }
}