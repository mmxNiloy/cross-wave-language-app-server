package com.crosswaveconsultancy.language_server.features.userProgress

import com.crosswaveconsultancy.language_server.features.userProgress.dto.UserProgressDto
import com.crosswaveconsultancy.language_server.features.userProgress.dto.UserProgressFilterDto
import com.crosswaveconsultancy.language_server.util.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/v1/user-progress")
@Validated
@Tag(
    name = "User Progress",
    description = "Store user progress by lesson id. Get a list of user progress, filter down the list with query params."
)
class UserProgressController(private val userProgressService: UserProgressService) {
    @GetMapping
    fun getUserProgress(
        @RequestHeader("X-Firebase-User-Id") @NotNull @NotBlank firebaseUserId: String,
        @Valid @ParameterObject @RequestParam filters: UserProgressFilterDto
    ): ApiResponse<List<UserProgressDto>> {
        val payload = userProgressService.getUserProgress(firebaseUserId, filters)

        return ApiResponse(
            status = 200,
            ok = true,
            message = "User progress list retrieved successfully",
            payload = payload,
            path = "/v1/user-progress/"
        )
    }

    @PostMapping("/{lessonId}")
    fun createUserProgress(
        @RequestHeader("X-Firebase-User-Id") @NotNull @NotBlank firebaseUserId: String,
        @PathVariable @NotNull @Min(0) lessonId: Long
    ): ResponseEntity<ApiResponse<UserProgressDto>> {
        val payload = userProgressService.createUserProgress(firebaseUserId, lessonId)

        return ResponseEntity.created(URI.create("/v1/user-progress"))
            .body(
                ApiResponse(
                    status = 201,
                    ok = true,
                    message = "User progress for lesson id: $lessonId has been saved.",
                    payload = payload,
                    path = "/v1/user-progress/$lessonId"
                )
            )
    }
}