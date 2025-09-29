package com.crosswaveconsultancy.language_server.features.language

import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseMinimalDto
import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "language")
@SQLRestriction("is_active = true")
data class LanguageEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String,
    var shortCode: String,
    var isActive: Boolean=true,
    @CreatedDate
    @Column(name = "created_at", updatable = false, insertable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @LastModifiedDate
    @Column(name="updated_at")
    val updatedAt: LocalDateTime? = LocalDateTime.now()
) {
    fun toDto(): LanguageResponseDto {
        return LanguageResponseDto(
            id = id,
            name = name,
            shortCode = shortCode,
            createdAt = createdAt?:LocalDateTime.now(),
            updatedAt = updatedAt?:LocalDateTime.now(),
            isActive = isActive
        )
    }

    fun toMinimalDto(): LanguageResponseMinimalDto {
        return LanguageResponseMinimalDto(
            name = name,
            shortCode = shortCode
        )
    }
}