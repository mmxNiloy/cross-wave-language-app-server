package com.crosswaveconsultancy.language_server.features.language

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class LanguageService(
    private val languageRepository: LanguageRepository
) {
    fun getLanguageById(id: Long): Optional<LanguageEntity> {
        return languageRepository.findById(id)
    }

    fun getLanguages(page: Int, limit: Int): List<LanguageEntity> {
        var pageable: Pageable = PageRequest.of(page - 1, limit)

        return languageRepository.findAll(pageable).toList()
    }

    fun count(): Long {
        return languageRepository.count()
    }

    fun save(languageEntity: LanguageEntity): LanguageEntity {
        return languageRepository.save(languageEntity)
    }
}