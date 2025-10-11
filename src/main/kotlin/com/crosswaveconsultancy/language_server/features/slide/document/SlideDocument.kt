package com.crosswaveconsultancy.language_server.features.slide.document

import com.crosswaveconsultancy.language_server.features.slide.dto.ComponentResponseDto
import com.crosswaveconsultancy.language_server.features.slide.dto.SectionResponseDto
import com.crosswaveconsultancy.language_server.features.slide.dto.SlideResponseDto
import com.crosswaveconsultancy.language_server.features.slide.dto.editor.EditorNodeDto
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.Optional

@Document(collection = "slide")
@CompoundIndex(
    name = "lesson_order_idx",
    def = "{'lessonId': 1, 'orderIndex': 1}",
    unique = true
)
@EnableMongoAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
data class SlideDocument(
    @Id
    val id: String = ObjectId().toHexString(),
    var lessonId: Long, // Foreign key to RDBMS -> Lesson Table
    var orderIndex: Int,
    var title: String,
    var isActive: Boolean = true,

    @CreatedDate
    var createdAt: LocalDateTime? = null,
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,

    var data: Map<String, EditorNodeDto> = emptyMap(),
    var previewImage: String? = null
) {
    fun toDto(): SlideResponseDto {
        var section: SectionResponseDto = data["ROOT"]?.toSectionDto(data)?:SectionResponseDto(
            displayName = "Unknown",
            props = emptyMap(),
            components = emptyList()
        )
        return SlideResponseDto(
            id = id,
            lessonId = lessonId,
            orderIndex = orderIndex,
            title = title,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt,
            previewImage = previewImage,
            section = section
        )
    }

    @Bean(name=["auditingDateTimeProvider"])
    fun auditingDateTimeProvider(): DateTimeProvider {
        return DateTimeProvider { Optional.of(LocalDateTime.now()) }
    }
}