package com.crosswaveconsultancy.language_server.features.module

import com.crosswaveconsultancy.language_server.features.language.LanguageEntity
import com.crosswaveconsultancy.language_server.features.module.dto.ModuleResponseDto
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity
@Table(name="language_module")
@SQLRestriction("is_active = true")
data class ModuleEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var description: String,
    var isActive: Boolean = true,
    @Column(name = "language_id")
    var languageId: Long,

    @CreatedDate
    @Column(name = "created_at", updatable = false, insertable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = LocalDateTime.now(),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="language_id", insertable = false, updatable = false)
    var language: LanguageEntity? = null
) {
    fun toDto(): ModuleResponseDto {
        return ModuleResponseDto(
            id = id,
            title = title,
            description = description,
            isActive = isActive,
            createdAt = createdAt?:LocalDateTime.now(),
            updatedAt = updatedAt?:LocalDateTime.now(),
            languageId = languageId,
            language = language?.toMinimalDto()
        )
    }

}