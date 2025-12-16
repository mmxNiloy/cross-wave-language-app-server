package com.crosswaveconsultancy.language_server.features.chapter

import com.crosswaveconsultancy.language_server.features.chapter.dto.ChapterResponseDto
import com.crosswaveconsultancy.language_server.features.chapter.dto.ChapterResponseMinimalDto
import com.crosswaveconsultancy.language_server.features.course.CourseEntity
import com.crosswaveconsultancy.language_server.features.lesson.LessonEntity
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
@Table(name = "chapter", uniqueConstraints = [
    UniqueConstraint(columnNames = ["course_id", "order_index"])
])
@EntityListeners(AuditingEntityListener::class)
@SQLRestriction("is_active = true")
data class ChapterEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var description: String,
    var isActive: Boolean,
    @Column(name = "created_at", updatable = false)
    @CreatedDate
    val createdAt: LocalDateTime? = null,
    @Column(name = "updated_at")
    @LastModifiedDate
    var updatedAt: LocalDateTime ? = null,
    var orderIndex: Int = 0,

    @Column(name="course_id")
    var courseId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference
    val course: CourseEntity? = null,

    @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY)
    @JsonManagedReference
    val lessons: List<LessonEntity> = emptyList(),
) {
    fun toDto(lessonCount: Long): ChapterResponseDto {
        return ChapterResponseDto(
            id = id,
            title = title,
            courseId=courseId,
            description = description,
            orderIndex = orderIndex,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt,
            lessonCount = lessonCount,
        )
    }

    fun toDto(): ChapterResponseDto {
        return ChapterResponseDto(
            id = id,
            title = title,
            courseId=courseId,
            description = description,
            orderIndex = orderIndex,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt,
            lessonCount = 0,
        )
    }

    fun toDtoWithLessons(): ChapterResponseDto {
        return ChapterResponseDto(
            id = id,
            title = title,
            courseId=courseId,
            description = description,
            orderIndex = orderIndex,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt,
            lessonCount = lessons.size.toLong(),
            lessons = lessons.map { it.toMinimalDto() },
        )
    }

    fun toMinimalDto(): ChapterResponseMinimalDto {
        return ChapterResponseMinimalDto(
            id = id,
            title = title,
            description = description,
        )
    }
}
