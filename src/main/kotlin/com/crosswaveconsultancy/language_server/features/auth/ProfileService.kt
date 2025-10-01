package com.crosswaveconsultancy.language_server.features.auth

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.auth.dto.ProfileResponseDto
import com.crosswaveconsultancy.language_server.features.auth.entity.ProfileEntity
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val profileRepository: ProfileRepository
) {
    fun getProfile(id: String): ProfileEntity {
        return profileRepository.findById(id).orElseThrow { ResourceNotFoundException("Profile not found with id $id") }
    }
}