package com.crosswaveconsultancy.language_server.features.module

import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.module.dto.CreateModuleDto
import com.crosswaveconsultancy.language_server.features.module.dto.ModuleResponseDto
import com.crosswaveconsultancy.language_server.features.module.dto.UpdateModuleDto
import com.crosswaveconsultancy.language_server.util.buildPaginationMetadata
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/module")
class ModuleController(
    private val moduleService: ModuleService,
) {
    @GetMapping
    fun findAll(
        @RequestParam page: Int?,
        @RequestParam limit: Int?,
        @RequestParam search: String?,
        @RequestParam order: Sort.Direction?
    ): ApiResponsePaginated<ModuleResponseDto> {
        val payload = moduleService.findAll(page ?: 1, limit ?: 10).map({ it.toDto() })
        val totalCount = moduleService.count()
        val paginationMetadata = buildPaginationMetadata(page ?: 1, limit ?: 10, payload.size, totalCount)

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
    fun findById(@PathVariable id: Long): ApiResponse<ModuleResponseDto> {
        val payload = moduleService.findById(id)

        if (payload.isEmpty) {
            throw ResourceNotFoundException("Module not found with id $id")
        }

        val response = ApiResponse<ModuleResponseDto>(
            ok = true,
            status = 200,
            message = "Module fetched successfully",
            payload = payload.get().toDto(),
            path = "/v1/module/$id"
        )
        return response
    }

    @PostMapping
    fun createModule(@RequestBody createModuleDto: CreateModuleDto): ResponseEntity<ApiResponse<ModuleResponseDto>> {
        println("createModuleDto: $createModuleDto")

        var payload = moduleService.save(createModuleDto)

        var response = ApiResponse<ModuleResponseDto>(
            ok = true,
            status = 201,
            message = "Module created successfully",
            payload = payload.toDto(),
            path = "/v1/module"
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PatchMapping("/{id}")
    fun updateModule(
        @PathVariable id: Long,
        @RequestBody createModuleDto: UpdateModuleDto
    ): ResponseEntity<ApiResponse<ModuleResponseDto>> {
        try {

            val payload = moduleService.update(id, createModuleDto);

            val response = ApiResponse<ModuleResponseDto>(
                ok = true,
                status = 200,
                message = "Module updated successfully",
                payload = payload.toDto(),
                path = "/v1/module/$id"
            )
            return ResponseEntity.ok(response)
        } catch (_: NoSuchElementException) {
            throw ResourceNotFoundException("Module not found with id $id")
        }
    }
}