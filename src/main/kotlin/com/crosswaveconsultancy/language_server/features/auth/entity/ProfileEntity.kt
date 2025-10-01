package com.crosswaveconsultancy.language_server.features.auth.entity

import com.crosswaveconsultancy.language_server.features.auth.dto.ProfileResponseDto
import com.crosswaveconsultancy.language_server.features.auth.dto.UserRole
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "profiles")
data class ProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val role: String,
) {
    fun toDto(): ProfileResponseDto {
        return ProfileResponseDto(
            name = "$firstName $lastName",
            role = role,
        )
    }
}