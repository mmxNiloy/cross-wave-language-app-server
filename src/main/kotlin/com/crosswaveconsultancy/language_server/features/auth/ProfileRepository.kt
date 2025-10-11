package com.crosswaveconsultancy.language_server.features.auth

import com.crosswaveconsultancy.language_server.features.auth.entity.ProfileEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProfileRepository: JpaRepository<ProfileEntity, UUID> {
}