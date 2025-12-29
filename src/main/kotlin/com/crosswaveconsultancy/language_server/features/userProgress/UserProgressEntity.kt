package com.crosswaveconsultancy.language_server.features.userProgress

import com.crosswaveconsultancy.language_server.features.userProgress.dto.UserProgressDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name="user_progress")
@EntityListeners(AuditingEntityListener::class)
open class UserProgressEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open val id: UUID? = null,

    @field:NotNull
    @field:NotBlank
    open var userId: String,
    @field:Min(0)
    open var courseId: Long,
    @field:Min(0)
    open var chapterId: Long,
    @field:Min(0)
    open var lessonId: Long,

    @CreatedDate
    @Column(updatable = false)
    open val createdAt: LocalDateTime? = null,
    @LastModifiedDate
    open var updatedAt: LocalDateTime? = null
) {
    fun toDto(): UserProgressDto {
        return UserProgressDto(
            id = id.toString(),
            userId,
            courseId,
            chapterId,
            lessonId,
            createdAt,
            updatedAt,
        )
    }
}