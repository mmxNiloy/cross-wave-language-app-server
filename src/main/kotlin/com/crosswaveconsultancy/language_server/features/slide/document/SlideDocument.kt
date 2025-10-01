package com.crosswaveconsultancy.language_server.features.slide.document

import com.crosswaveconsultancy.language_server.features.slide.dto.SlideResponseDto
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection="slide")
@CompoundIndex(
    name = "lesson_order_idx",
    def = "{'lessonId': 1, 'orderIndex': 1}",
    unique = true
)
data class SlideDocument(
    @Id
    val id: String = ObjectId().toHexString(),
    var lessonId: Long, // Foreign key to RDBMS -> Lesson Table
    var orderIndex: Int,
    var title: String,
    var isActive: Boolean=true,

    @CreatedDate
    var createdAt: LocalDateTime=LocalDateTime.now(),
    @LastModifiedDate
    var updatedAt: LocalDateTime=LocalDateTime.now(),

    var sections: List<SectionDocument> = emptyList()
) {
    fun toDto(): SlideResponseDto {
        return SlideResponseDto(
            id = id,
            lessonId = lessonId,
            orderIndex = orderIndex,
            title = title,
            isActive=isActive,
            createdAt=createdAt,
            updatedAt=updatedAt,
            sections = sections.map {it.toDto()}
        )
    }
}