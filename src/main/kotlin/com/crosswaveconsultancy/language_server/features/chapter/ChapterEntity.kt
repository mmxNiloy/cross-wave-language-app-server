package com.crosswaveconsultancy.language_server.features.chapter

import com.crosswaveconsultancy.language_server.features.chapter.dto.ChapterResponseDto
import com.crosswaveconsultancy.language_server.features.chapter.dto.ChapterResponseMinimalDto
import com.crosswaveconsultancy.language_server.features.course.CourseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "chapter")
data class ChapterEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var description: String,
    var isActive: Boolean,
    @Column(name = "created_at", insertable = false, updatable = false)
    @CreatedDate
    val createdAt: LocalDateTime,
    @Column(name = "updated_at")
    @LastModifiedDate
    var updatedAt: LocalDateTime,
    var orderIndex: Int,

    @Column(name="course_id")
    var courseId: Long,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "course_id", referencedColumnName = "id", insertable = false, updatable = false)
    var course: CourseEntity? = null,
) {
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
            course = course?.toMinimalDto()
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
