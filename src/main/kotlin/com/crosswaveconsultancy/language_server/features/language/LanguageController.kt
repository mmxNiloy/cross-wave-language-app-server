package com.crosswaveconsultancy.language_server.features.language

import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.language.dto.LanguageDTO
import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import com.crosswaveconsultancy.language_server.util.buildPaginationMetadata
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/language")
class LanguageController(
    private val languageService: LanguageService
) {
    @GetMapping
    fun getLanguages(
        @RequestParam page: Int?,
        @RequestParam limit: Int?,
        @RequestParam search: String?,
        @RequestParam order: Sort.Direction?
    ): ResponseEntity<ApiResponsePaginated<LanguageResponseDto>> {
        var payload = languageService.getLanguages(page ?: 1, limit ?: 10).map({it.toDto()});
        var totalCount = languageService.count();
        var paginationMetadata = buildPaginationMetadata(page?:1, limit?:10, payload.size, totalCount)

        var response = ApiResponsePaginated<LanguageResponseDto>(
            status = 200,
            ok = true,
            message = "Languages fetched successfully",
            payload = payload,
            path = "/v1/language",
            paginationMetadata = paginationMetadata
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getLanguage(@PathVariable id: Long): ResponseEntity<ApiResponse<LanguageResponseDto>> {
        var payload = languageService.getLanguageById(id)

        if(payload.isEmpty) {
            throw ResourceNotFoundException("Language not found with id $id")
        }

        var response = ApiResponse<LanguageResponseDto>(
            status = 200,
            ok = true,
            message = "Language fetched successfully",
            payload = payload.get().toDto(),
            path = "/v1/language/$id"
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping
    fun createLanguage(@RequestBody languageDto: LanguageDTO): ResponseEntity<ApiResponse<LanguageResponseDto>> {
        var payload = languageService.save(languageDto.toEntity())

        var response = ApiResponse<LanguageResponseDto>(
            status = 200,
            ok = true,
            message = "Language created successfully",
            payload = payload.toDto(),
            path = "/v1/language"
        )
        return ResponseEntity.ok(response)
    }
}