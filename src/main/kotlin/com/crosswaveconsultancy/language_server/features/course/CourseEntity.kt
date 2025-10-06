package com.crosswaveconsultancy.language_server.features.course

import com.crosswaveconsultancy.language_server.features.chapter.ChapterEntity
import com.crosswaveconsultancy.language_server.features.course.dto.CourseResponseDto
import com.crosswaveconsultancy.language_server.features.course.dto.CourseResponseMinimalDto
import com.crosswaveconsultancy.language_server.features.language.LanguageEntity
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(
    name = "course",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["language_code", "order_index"])
    ]
)
@EntityListeners(AuditingEntityListener::class)
@SQLRestriction("is_active = true")
data class CourseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var description: String,
    var orderIndex: Int=0,
    var isActive: Boolean=true,

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @Column(name = "language_code")
    val languageCode: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_code", insertable = false, updatable = false)
    @JsonBackReference
    val language: LanguageEntity? = null,

    @OneToMany(mappedBy="course", fetch= FetchType.LAZY)
    @JsonManagedReference
    val chapters: List<ChapterEntity> = emptyList()
) {
    fun toDto(chapterCount: Long?): CourseResponseDto {
        return CourseResponseDto(
            id = id,
            title = title,
            description = description,
            orderIndex = orderIndex,
            isActive = isActive,
            createdAt = createdAt ?: LocalDateTime.now(),
            updatedAt = updatedAt ?: LocalDateTime.now(),
            languageCode = languageCode,
            language = language?.toDto(),
            chapterCount = chapterCount?:0
        )
    }

    fun toDto(): CourseResponseDto {
        return CourseResponseDto(
            id = id,
            title = title,
            description = description,
            orderIndex = orderIndex,
            isActive = isActive,
            createdAt = createdAt ?: LocalDateTime.now(),
            updatedAt = updatedAt ?: LocalDateTime.now(),
            languageCode = languageCode,
            language = language?.toDto(),
            chapterCount = 0
        )
    }

    fun toDtoWithChapters(): CourseResponseDto {
        return CourseResponseDto(
            id = id,
            title = title,
            description = description,
            orderIndex = orderIndex,
            isActive = isActive,
            createdAt = createdAt ?: LocalDateTime.now(),
            updatedAt = updatedAt ?: LocalDateTime.now(),
            languageCode = languageCode,
            language = language?.toDto(),
            chapterCount = chapters.size.toLong(),
            chapters = chapters.map { it.toMinimalDto() }
        )
    }

    fun toMinimalDto(): CourseResponseMinimalDto {
        return CourseResponseMinimalDto(
            id = id,
            title = title,
            description = description,
            isActive = isActive,
        )
    }
}