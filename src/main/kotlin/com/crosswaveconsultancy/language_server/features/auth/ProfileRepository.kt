package com.crosswaveconsultancy.language_server.features.auth

import com.crosswaveconsultancy.language_server.features.auth.entity.ProfileEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProfileRepository: JpaRepository<ProfileEntity, String> {
}