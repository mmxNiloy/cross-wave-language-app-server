package com.crosswaveconsultancy.language_server.features.course

import com.crosswaveconsultancy.language_server.features.course.dto.CourseResponseDto
import com.crosswaveconsultancy.language_server.features.course.dto.CourseResponseMinimalDto
import com.crosswaveconsultancy.language_server.features.module.ModuleEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "course",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["module_id", "order_index"])
    ])
data class CourseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var description: String,
    var orderIndex: Int,
    var isActive: Boolean,

    @CreatedDate
    @Column(name = "created_at", insertable = false, updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @LastModifiedDate
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = LocalDateTime.now(),

    @Column(name="module_id")
    val moduleId: Long,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "module_id", insertable = false, updatable = false)
    val module: ModuleEntity? = null
) {
    fun toDto(): CourseResponseDto {
        return CourseResponseDto(
            id = id,
            title = title,
            description = description,
            orderIndex = orderIndex,
            isActive = isActive,
            createdAt = createdAt?:LocalDateTime.now(),
            updatedAt = updatedAt?:LocalDateTime.now(),
            moduleId = moduleId,
            module = module?.toDto()
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