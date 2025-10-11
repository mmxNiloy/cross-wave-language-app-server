package com.crosswaveconsultancy.language_server.features.lesson

import com.crosswaveconsultancy.language_server.features.chapter.ChapterEntity
import com.crosswaveconsultancy.language_server.features.lesson.dto.LessonResponseDto
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "lesson", uniqueConstraints = [
    UniqueConstraint(columnNames = ["chapter_id", "order_index"])
])
@EntityListeners(AuditingEntityListener::class)
@SQLRestriction("is_active = true")
data class LessonEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var description: String,
    @Column(name = "chapter_id")
    var chapterId: Long,
    var orderIndex: Int=0,
    var isActive: Boolean = true,

    @CreatedDate
    @Column(name = "created_at", insertable = false)
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "chapter_id", insertable = false, updatable = false)
    @JsonBackReference
    val chapter: ChapterEntity?=null,
) {
    fun toDto() : LessonResponseDto {
        return LessonResponseDto(
            id=id,
            title=title,
            description=description,
            chapterId=chapterId,
            orderIndex=orderIndex,
            isActive=isActive,
            createdAt=createdAt,
            updatedAt=updatedAt,
        )
    }
}