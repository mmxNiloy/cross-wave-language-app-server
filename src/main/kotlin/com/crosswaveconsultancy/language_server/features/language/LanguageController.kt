package com.crosswaveconsultancy.language_server.features.language

import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.features.language.dto.CreateLanguageDto
import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import com.crosswaveconsultancy.language_server.features.language.dto.UpdateLanguageDto
import com.crosswaveconsultancy.language_server.util.PaginationRequestParamsDto
import com.crosswaveconsultancy.language_server.util.buildPaginationMetadata
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

@RestController
@RequestMapping("/v1/language")
@Validated
class LanguageController(
    private val languageService: LanguageService
) {
    @GetMapping
    fun getLanguages(
        @Valid @ParameterObject params: PaginationRequestParamsDto
    ): ApiResponsePaginated<LanguageResponseDto> {
        val page = params.page ?: 1
        val limit = params.limit ?: 10

        var payload = languageService.getLanguages(page, limit).map { it.toDto() }
        var totalCount = languageService.count();
        var paginationMetadata = buildPaginationMetadata(page, limit, payload.size, totalCount)

        return ApiResponsePaginated<LanguageResponseDto>(
            status = 200,
            ok = true,
            message = "Languages fetched successfully",
            payload = payload,
            path = "/v1/language",
            paginationMetadata = paginationMetadata
        )
    }

    @GetMapping("/{id}")
    fun getLanguage(@PathVariable @Min(1) id: Long): ApiResponse<LanguageResponseDto> {
        var payload = languageService.getLanguageById(id)

        return ApiResponse<LanguageResponseDto>(
            status = 200,
            ok = true,
            message = "Language fetched successfully",
            payload = payload.toDto(),
            path = "/v1/language/$id"
        )
    }

    @PostMapping
    fun createLanguage(@Valid @RequestBody createLanguageDto: CreateLanguageDto): ResponseEntity<ApiResponse<LanguageResponseDto>> {
        var payload = languageService.save(createLanguageDto)

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse<LanguageResponseDto>(
                status = 201,
                ok = true,
                message = "Language created successfully",
                payload = payload.toDto(),
                path = "/v1/language"
            )
        )
    }

    @PatchMapping("/restore/{id}")
    fun restoreLanguage(@PathVariable @Min(1) id: Long): ApiResponse<LanguageResponseDto> {
        var payload = languageService.toggle(id, true)
        return ApiResponse<LanguageResponseDto>(
            status = 200,
            ok = true,
            message = "Language restored successfully",
            payload = payload.toDto(),
            path = "/v1/language/$id"
        )
    }

    @PatchMapping("/{id}")
    fun updateLanguage(@PathVariable @Min(1) id: Long, @Valid @RequestBody updateLanguageDto: UpdateLanguageDto): ApiResponse<LanguageResponseDto> {
        var payload = languageService.update(id, updateLanguageDto)
        return ApiResponse<LanguageResponseDto>(
            status = 200,
            ok = true,
            message = "Language updated successfully",
            payload = payload.toDto(),
            path = "/v1/language/$id"
        )
    }

    @DeleteMapping("/{id}")
    fun deleteLanguage(@PathVariable @Min(1) id: Long): ResponseEntity<ApiResponse<String>> {
        var payload = languageService.toggle(id, false)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse(
            ok = true,
            status = 204,
            message = "Language deleted successfully",
            payload = "Language deleted successfully",
            path = "/v1/language/$id"
        ))
    }
}