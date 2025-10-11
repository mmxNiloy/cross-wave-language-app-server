package com.crosswaveconsultancy.language_server.features.language

import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseMinimalDto
import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "language")
@SQLRestriction("is_active = true")
@EntityListeners(AuditingEntityListener::class)
data class LanguageEntity(
    @Id
    val shortCode: String,
    var languageName: String,
    var title: String = "",
    var description: String = "",
    var isActive: Boolean=true,
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name="updated_at")
    val updatedAt: LocalDateTime? = null
) {
    fun toDto(): LanguageResponseDto {
        return LanguageResponseDto(
            languageName = languageName,
            shortCode = shortCode,
            createdAt = createdAt?:LocalDateTime.now(),
            updatedAt = updatedAt?:LocalDateTime.now(),
            isActive = isActive,
            title = title,
            description = description
        )
    }
}