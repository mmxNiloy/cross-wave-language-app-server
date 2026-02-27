package com.crosswaveconsultancy.language_server.features.kanji

import com.crosswaveconsultancy.language_server.features.kanji.dto.KanjiResponseDto
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(
    name = "kanji"
)
class KanjiEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var character: String,
    var filename: String,
    var unicodeName: String,
    var unicode: String,
    var category: String,
    var variant: String?
) {
    fun toDto(): KanjiResponseDto {
        return KanjiResponseDto(
            id,
            character,
            filename,
            unicodeName,
            unicode,
            category,
            variant
        )
    }
}