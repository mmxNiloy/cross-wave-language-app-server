package com.crosswaveconsultancy.language_server.features.kanji

import com.crosswaveconsultancy.language_server.features.kanji.dto.KanjiResponseDto
import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.util.PaginationMetadata
import com.crosswaveconsultancy.language_server.util.PaginationRequestParamsDto
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springdoc.core.annotations.ParameterObject
import org.springframework.cache.annotation.CachePut
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(
    name = "Kanji Library",
    description = "Read operations for Kanji character library"
)
@RestController
@RequestMapping("/api/v1/kanji")
@Validated
class KanjiController(
    private val kanjiService: KanjiService
) {
    @CachePut(value=["kanji-paged"])
    @GetMapping
    fun getKanjiList(
        @Valid @ParameterObject params: PaginationRequestParamsDto
    ): ApiResponsePaginated<KanjiResponseDto> {
        val page = params.page ?: 1
        val limit = params.limit ?: 10

        // TODO: Search feature
        val pageData = kanjiService.getKanjiPage(page - 1, limit)

        return ApiResponsePaginated(
            status = HttpStatus.OK.value(),
            ok = true,
            message = "Retrieved a list of Kanji entries.",
            payload = pageData.content.map { it.toDto() },
            path = "/api/v1/kanji",
            paginationMetadata = PaginationMetadata(
                page,
                limit,
                dataCount = pageData.content.size,
                totalCount = pageData.totalElements,
                pageCount = pageData.totalPages,
                hasNext = pageData.hasNext(),
                hasPrevious = pageData.hasPrevious(),
                next = (page + 1).coerceAtMost(pageData.totalPages),
                previous = (page - 1).coerceAtLeast(1),
                first = 1,
                last = pageData.totalPages
            )
        )
    }

    @CachePut(value = ["kanji-list"])
    @GetMapping("/all")
    fun getAllKanjiList(): ApiResponse<List<KanjiResponseDto>> {
        val payload = kanjiService.getAllKanjiList()

        return ApiResponse(
            status = HttpStatus.OK.value(),
            ok = true,
            message = "Retrieved all kanji entries.",
            payload = payload,
            path = "/api/v1/kanji/all"
        )
    }

    @CachePut(value = ["kanji"])
    @GetMapping("/{id}")
    fun getKanjiById(@PathVariable @Valid @Min(1) id: Long): ApiResponse<KanjiResponseDto> {
        val payload = kanjiService.getKanjiById(id)

        return ApiResponse(
            status = HttpStatus.OK.value(),
            ok = true,
            message = "Kanji Entry found.",
            payload = payload,
            path = "/api/v1/kanji/$id"
        )
    }
}