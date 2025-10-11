package com.crosswaveconsultancy.language_server.features.auth

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.auth.dto.ProfileResponseDto
import com.crosswaveconsultancy.language_server.features.auth.entity.ProfileEntity
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProfileService(
    private val profileRepository: ProfileRepository
) {
    fun getProfile(id: String): ProfileEntity {
        return profileRepository.findById(UUID.fromString(id)).orElseThrow { ResourceNotFoundException("Profile not found with id $id") }
    }
}