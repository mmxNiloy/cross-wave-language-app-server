package com.crosswaveconsultancy.language_server.features.language

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.language.dto.CreateLanguageDto
import com.crosswaveconsultancy.language_server.features.language.dto.UpdateLanguageDto
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LanguageService(
    private val languageRepository: LanguageRepository
) {
    fun getLanguageById(id: Long): LanguageEntity {
        return languageRepository.findById(id).orElseThrow { ResourceNotFoundException("Language not found with id $id") }
    }

    fun getLanguages(page: Int, limit: Int): List<LanguageEntity> {
        var pageable: Pageable = PageRequest.of(page - 1, limit)

        return languageRepository.findAll(pageable).toList()
    }

    fun count(): Long {
        return languageRepository.count()
    }

    fun save(languageDto: CreateLanguageDto): LanguageEntity {
        return languageRepository.save(languageDto.toEntity())
    }

    fun update(langaugeId: Long, languageDto: UpdateLanguageDto): LanguageEntity {
        var language = getLanguageById(langaugeId)

        if(languageDto.name != null) language.name = languageDto.name
        if(languageDto.shortCode != null) language.shortCode = languageDto.shortCode

        return languageRepository.save(language)
    }

    @Transactional
    fun toggle(id: Long, active: Boolean): LanguageEntity {
        var language = getLanguageById(id)
        language.isActive = active
        return languageRepository.save(language)
    }
}