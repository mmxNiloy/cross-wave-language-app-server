package com.crosswaveconsultancy.language_server.features.auth

import com.crosswaveconsultancy.language_server.features.auth.dto.ProfileResponseDto
import com.crosswaveconsultancy.language_server.util.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.oauth2.jwt.Jwt;


@RestController
@RequestMapping("/api/v1/profile")
class ProfileController(
    private val profileService: ProfileService
) {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-profile")
    fun getMyProfile(@AuthenticationPrincipal jwt: Jwt): ApiResponse<ProfileResponseDto> {
        val userId = jwt.subject
        val profile = profileService.getProfile(userId)

        return ApiResponse(
            status = 200,
            ok = true,
            message = "Profile fetched successfully",
            payload = profile.toDto(),
            path = "/v1/profile"
        )
    }
}