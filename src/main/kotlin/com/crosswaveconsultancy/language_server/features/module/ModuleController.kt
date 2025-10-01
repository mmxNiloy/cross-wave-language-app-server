package com.crosswaveconsultancy.language_server.features.module

import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.features.module.dto.CreateModuleDto
import com.crosswaveconsultancy.language_server.features.module.dto.ModuleResponseDto
import com.crosswaveconsultancy.language_server.features.module.dto.ModuleStatsDto
import com.crosswaveconsultancy.language_server.features.module.dto.UpdateModuleDto
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

@Tag(
    name = "Module Management",
    description = "CRUD for modules. A module represents a language tutorial; ie: English language tutorial, Japanese language tutorial, French language tutorial, etc. A module contains some courses."
)
@RestController
@RequestMapping("/api/v1/module")
@Validated
class ModuleController(
    private val moduleService: ModuleService,
) {
    @GetMapping
    fun findAll(
        @Valid @ParameterObject params: PaginationRequestParamsDto
    ): ApiResponsePaginated<ModuleResponseDto> {
        val page = params.page ?: 1
        val limit = params.limit ?: 10
        val payload = moduleService.findAll(page, limit).map { it.toDto() }
        val totalCount = moduleService.count()
        val paginationMetadata = buildPaginationMetadata(page, limit, payload.size, totalCount)

        return ApiResponsePaginated<ModuleResponseDto>(
            ok = true,
            status = 200,
            message = "Modules fetched successfully",
            payload = payload,
            path = "/v1/module",
            paginationMetadata = paginationMetadata
        )
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable @Min(1) id: Long): ApiResponse<ModuleResponseDto> {
        val payload = moduleService.findById(id)

        return ApiResponse<ModuleResponseDto>(
            ok = true,
            status = 200,
            message = "Module fetched successfully",
            payload = payload.toDto(),
            path = "/v1/module/$id"
        )
    }

    @GetMapping("/{id}/stat")
    fun getModuleStats(@PathVariable @Min(1) id: Long): ApiResponse<ModuleStatsDto> {
        val payload = moduleService.getModuleStats(id)

        return ApiResponse<ModuleStatsDto>(
            ok = true,
            status = 200,
            message = "Module stats fetched successfully",
            payload = payload,
            path = "/v1/module/$id/stat"
        )
    }

    @PostMapping
    fun createModule(@Valid @RequestBody createModuleDto: CreateModuleDto): ResponseEntity<ApiResponse<ModuleResponseDto>> {
        var payload = moduleService.save(createModuleDto)

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse<ModuleResponseDto>(
                ok = true,
                status = 201,
                message = "Module created successfully",
                payload = payload.toDto(),
                path = "/v1/module"
            )
        )
    }

    @PatchMapping("/restore/{id}")
    fun restoreModule(@PathVariable @Min(1) id: Long): ResponseEntity<ApiResponse<ModuleResponseDto>> {
        var payload = moduleService.toggle(id, true)
        return ResponseEntity.ok(
            ApiResponse<ModuleResponseDto>(
                ok = true,
                status = 200,
                message = "Module restored successfully",
                payload = payload.toDto(),
                path = "/v1/module/$id"
            )
        )

    }

    @PatchMapping("/{id}")
    fun updateModule(
        @PathVariable id: Long,
        @Valid @RequestBody updateModuleDto: UpdateModuleDto
    ): ResponseEntity<ApiResponse<ModuleResponseDto>> {
        val payload = moduleService.update(id, updateModuleDto);

        return ResponseEntity.ok(
            ApiResponse<ModuleResponseDto>(
                ok = true,
                status = 200,
                message = "Module updated successfully",
                payload = payload.toDto(),
                path = "/v1/module/$id"
            )
        )
    }

    @DeleteMapping("/{id}")
    fun deleteModule(@PathVariable @Min(1) id: Long): ResponseEntity<ApiResponse<String>> {
        var payload = moduleService.toggle(id, false)
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(
                ApiResponse<String>(
                    ok = true,
                    status = 204,
                    message = "Module deleted successfully",
                    payload = "Module deleted successfully",
                    path = "/v1/module/$id"
                )
            )
    }

}