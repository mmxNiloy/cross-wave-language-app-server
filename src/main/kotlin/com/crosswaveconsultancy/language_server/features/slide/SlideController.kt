package com.crosswaveconsultancy.language_server.features.slide

import com.crosswaveconsultancy.language_server.features.slide.document.SlideDocument
import com.crosswaveconsultancy.language_server.features.slide.dto.CreateSlideDto
import com.crosswaveconsultancy.language_server.features.slide.dto.SlideResponseBaseDto
import com.crosswaveconsultancy.language_server.features.slide.dto.SlideResponseDto
import com.crosswaveconsultancy.language_server.features.slide.dto.SwapSlideOrderIndexDto
import com.crosswaveconsultancy.language_server.features.slide.dto.UpdateSlideDto
import com.crosswaveconsultancy.language_server.util.ApiResponse
import com.crosswaveconsultancy.language_server.util.ApiResponsePaginated
import com.crosswaveconsultancy.language_server.util.PaginationRequestParamsDto
import com.crosswaveconsultancy.language_server.util.buildPaginationMetadata
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Slide Management", description = "CRUD for slides. Each slide is associated with a lesson. Each slide is composed of sections. Sections are UI components with layouts that can be used to display content. Each section in turn holds some components. Components are units of content that can be displayed in a section. Components can be of different types such as text, image, audio, video, mcq, etc. Components have differing properties that define their behavior and appearance.")
@RestController
@RequestMapping("/api/v1/slide")
@Validated
class SlideController(
    private val slideService: SlideService
) {
    @GetMapping
    fun getSlides(
        @Valid @ParameterObject params: PaginationRequestParamsDto,
        @Valid @RequestParam shouldMinify: Boolean? = false
    ): ApiResponsePaginated<SlideResponseBaseDto> {
        val page = params.page?:1
        val limit = params.limit?:10

        val slides = slideService.getSlides(page, limit, shouldMinify)
        val count = slideService.count()

        val paginationMetadata = buildPaginationMetadata(page, limit, slides.size, count)

        return ApiResponsePaginated(
            ok = true,
            status = 200,
            message = "Slides fetched successfully",
            payload = slides,
            path = "/v1/slide",
            paginationMetadata = paginationMetadata
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/web/lesson/{id}")
    fun getSlidesForEditorByLessonId(
        @Valid @ParameterObject params: PaginationRequestParamsDto,
        @PathVariable @Min(1) id: Long
    ): ApiResponsePaginated<SlideDocument> {
        val page = params.page?:1
        val limit = params.limit?:10

        val slides = slideService.getSlidesByLessonId(id, page, limit)
        val count = slideService.countByLessonId(id)

        val paginationMetadata = buildPaginationMetadata(page, limit, slides.size, count)

        return ApiResponsePaginated(
            ok = true,
            status = 200,
            message = "Slides fetched successfully",
            payload = slides,
            path = "/v1/slide/web/lesson/$id",
            paginationMetadata = paginationMetadata
        )
    }

    @GetMapping("/lesson/{id}")
    fun getSlidesByLessonId(
        @Valid @ParameterObject params: PaginationRequestParamsDto,
        @PathVariable @Min(1) id: Long,
        @Valid @RequestParam shouldMinify: Boolean? = false
    ): ApiResponsePaginated<SlideResponseBaseDto> {
        val page = params.page?:1
        val limit = params.limit?:10

        val slides = slideService.getSlidesByLessonId(id, page, limit, shouldMinify)
        val count = slideService.countByLessonId(id)

        val paginationMetadata = buildPaginationMetadata(page, limit, slides.size, count)

        return ApiResponsePaginated(
            ok = true,
            status = 200,
            message = "Slides fetched successfully",
            payload = slides,
            path = "/v1/slide/lesson/$id",
            paginationMetadata = paginationMetadata
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/web/{id}")
    fun getSlideForEditorById(
        @PathVariable @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid ObjectId format") id: String
    ): ApiResponse<SlideDocument> {
        val slide = slideService.getSlideById(id)
        return ApiResponse(
            ok = true,
            status = 200,
            message = "Slide fetched successfully",
            payload = slide,
            path = "/v1/slide/web/$id"
        )
    }

    @GetMapping("/{id}")
    fun getSlideById(
        @PathVariable @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid ObjectId format") id: String,
        @Valid @RequestParam shouldMinify: Boolean? = false
    ): ApiResponse<SlideResponseBaseDto> {
        val slide = slideService.getSlideById(id, shouldMinify)
        return ApiResponse<SlideResponseBaseDto>(
            ok = true,
            status = 200,
            message = "Slide fetched successfully",
            payload = slide,
            path = "/v1/slide/$id"
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createSlide(
        @Valid @RequestBody slideDto: CreateSlideDto): ResponseEntity<ApiResponse<SlideResponseDto>>
    {
        val slide = slideService.createSlide(slideDto)
        return ResponseEntity.ok(ApiResponse(
            ok = true,
            status = 200,
            message = "Slide created successfully",
            payload = slide.toDto(),
            path = "/v1/slide"
        ))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/swap-order-index")
    fun swapOrderIndex(@Valid @RequestBody dto: SwapSlideOrderIndexDto) : ApiResponse<List<SlideResponseDto>> {
        val payload = slideService.swapOrderIndex(dto).map {it.toDto()}

        return ApiResponse(
            ok=true,
            status=200,
            message="Swapped slide order indices",
            payload=payload,
            path="/v1/slide/swap-order-index"
        )
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PatchMapping("/restore/{id}")
//    fun restoreSlide(
//        @PathVariable @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid ObjectId format") id: String
//    ): ResponseEntity<Void> {
//        slideService.delete(id, true)
//        return ResponseEntity.noContent().build()
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    fun updateSlide(
        @PathVariable @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid ObjectId format") id: String,
        @Valid @RequestBody slideDto: UpdateSlideDto
    ): ApiResponse<SlideResponseDto> {
        val slide = slideService.updateSlide(id, slideDto)
        return ApiResponse(
            ok = true,
            status = 200,
            message = "Slide updated successfully",
            payload = slide.toDto(),
            path = "/v1/slide/$id"
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteSlide(
        @PathVariable @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid ObjectId format") id: String
    ): ResponseEntity<Void> {
        slideService.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}