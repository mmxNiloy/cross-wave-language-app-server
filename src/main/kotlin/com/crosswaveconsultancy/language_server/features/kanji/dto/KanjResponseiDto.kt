package com.crosswaveconsultancy.language_server.features.kanji.dto

data class KanjiResponseDto(
    val id: Long,
    var character: String,
    var filename: String,
    var unicodeName: String,
    var unicode: String,
    var category: String,
    var variant: String?
)