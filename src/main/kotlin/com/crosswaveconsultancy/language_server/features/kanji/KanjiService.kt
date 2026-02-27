package com.crosswaveconsultancy.language_server.features.kanji

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.kanji.dto.KanjiResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class KanjiService(
    private val kanjiRepository: KanjiRepository
) {
    fun getAllKanjiList(): List<KanjiResponseDto> {
        return kanjiRepository.findAll().map { it.toDto() }
    }

    fun getKanjiPage(
        page: Int,
        limit: Int,
    ): Page<KanjiEntity> {
        return kanjiRepository.findAll(PageRequest.of(page, limit))
    }

    fun getKanjiById(id: Long): KanjiResponseDto {
        return kanjiRepository.findById(id).orElseThrow { ResourceNotFoundException("Kanji Entry Not Found") }.toDto()
    }
}