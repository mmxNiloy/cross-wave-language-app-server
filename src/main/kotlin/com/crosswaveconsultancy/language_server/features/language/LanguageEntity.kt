package com.crosswaveconsultancy.language_server.features.language

import com.crosswaveconsultancy.language_server.features.language.dto.LanguageResponseDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity
@Table(name = "language")
data class LanguageEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val shortCode: String,
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
            updatedAt = updatedAt?:LocalDateTime.now()
        )
    }
}