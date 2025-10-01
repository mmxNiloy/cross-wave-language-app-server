package com.crosswaveconsultancy.language_server.features.chapter

import com.crosswaveconsultancy.language_server.features.chapter.dto.ChapterResponseDto
import com.crosswaveconsultancy.language_server.features.chapter.dto.CreateChapterDto
import com.crosswaveconsultancy.language_server.features.chapter.dto.UpdateChapterDto
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
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Chapter Management", description = "CRUD for chapters. Each chapter is associated with a course. Each chapter contains some lessons.")
@RestController
@RequestMapping("/api/v1/chapter")
@Validated
class ChapterController(
    private val chapterService: ChapterService
) {
    @GetMapping
    fun getChapters(@Valid @ParameterObject params: PaginationRequestParamsDto): ApiResponsePaginated<ChapterResponseDto> {
        val page = params.page?:1
        val limit = params.limit?:10
        val payload = chapterService.getChapters(page, limit).map { it.toDto() }
        val totalCount = chapterService.count()
        val paginationMetadata = buildPaginationMetadata(page, limit, payload.size, totalCount)

        return ApiResponsePaginated<ChapterResponseDto>(
            ok = true,
            status = 200,
            message = "Chapters fetched successfully",
            payload = payload,
            path = "/v1/chapter",
            paginationMetadata = paginationMetadata
        )
    }

    @GetMapping("/course/{id}")
    fun getChaptersByCourseId(
        @Valid @ParameterObject params: PaginationRequestParamsDto,
        @PathVariable @Min(1) id: Long
    ): ApiResponsePaginated<ChapterResponseDto> {
        val page = params.page?:1
        val limit = params.limit?:10
        val payload = chapterService.getChaptersByCourseId(id, page, limit).map { it.toDto() }
        val totalCount = chapterService.countByCourseId(id)
        val paginationMetadata = buildPaginationMetadata(page, limit, payload.size, totalCount)

        return ApiResponsePaginated<ChapterResponseDto>(
            ok = true,
            status = 200,
            message = "Chapters fetched successfully",
            payload = payload,
            path = "/v1/chapter/course/$id",
            paginationMetadata = paginationMetadata
        )
    }

    @GetMapping("/{id}")
    fun getChapterById(@PathVariable @Min(1) id: Long): ApiResponse<ChapterResponseDto> {
        val payload = chapterService.getChapterById(id)

        return ApiResponse(
            ok = true,
            status = 200,
            message = "Chapter fetched successfully",
            payload = payload.toDto(),
            path = "/v1/chapter/$id"
        )
    }

    @PostMapping
    fun createChapter(@Valid @RequestBody chapterDto: CreateChapterDto): ResponseEntity<ApiResponse<ChapterResponseDto>> {
        val payload = chapterService.save(chapterDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse(
            ok = true,
            status = 201,
            message = "Chapter created successfully",
            payload = payload.toDto(),
            path = "/v1/chapter"
        ))
    }

    @PatchMapping("/restore/{id}")
    fun restoreChapter(@PathVariable @Min(1) id: Long): ResponseEntity<ApiResponse<ChapterResponseDto>> {
        val payload = chapterService.toggle(id, true)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse(
            ok = true,
            status = 200,
            message = "Chapter restored successfully",
            payload = payload.toDto(),
            path = "/v1/chapter/$id"
        ))
    }

    @PatchMapping("/{id}")
    fun updateChapter(@PathVariable @Min(1) id: Long, @Valid @RequestBody chapterDto: UpdateChapterDto): ApiResponse<ChapterResponseDto> {
        val payload = chapterService.update(id, chapterDto)
        return ApiResponse(
            ok = true,
            status = 200,
            message = "Chapter updated successfully",
            payload = payload.toDto(),
            path = "/v1/chapter/$id"
        )
    }

    @DeleteMapping("/{id}")
    fun deleteChapter(@PathVariable @Min(1) id: Long): ResponseEntity<ApiResponse<String>> {
        val payload = chapterService.toggle(id, false)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse(
            ok = true,
            status = 204,
            message = "Chapter deleted successfully",
            payload = null,
            path = "/v1/chapter/$id"
        ))
    }
}