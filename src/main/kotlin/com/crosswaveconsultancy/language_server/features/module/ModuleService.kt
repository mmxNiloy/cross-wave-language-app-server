package com.crosswaveconsultancy.language_server.features.module

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.language.LanguageEntity
import com.crosswaveconsultancy.language_server.features.language.LanguageRepository
import com.crosswaveconsultancy.language_server.features.module.dto.CreateModuleDto
import com.crosswaveconsultancy.language_server.features.module.dto.UpdateModuleDto
import jakarta.persistence.EntityManager
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class ModuleService(
    private val moduleRepository: ModuleRepository,
    private val languageRepository: LanguageRepository,
    private val entityManager: EntityManager
) {
    fun findById(id: Long): Optional<ModuleEntity> {
        return moduleRepository.findById(id)
    }

    fun findAll(page: Int, size: Int): List<ModuleEntity> {
        var pageRequest = PageRequest.of(page - 1, size)
        return moduleRepository.findAll(pageRequest).toList()
    }

    fun save(createModuleDto: CreateModuleDto): ModuleEntity {
        val moduleEntity = ModuleEntity(
            title = createModuleDto.title,
            description = createModuleDto.description,
            languageId = createModuleDto.languageId
        )
        return moduleRepository.save(moduleEntity)
    }

    fun deleteById(id: Long) {
        moduleRepository.deleteById(id)
    }

    fun count(): Long {
        return moduleRepository.count()
    }

    fun update(id: Long, updateModuleDto: UpdateModuleDto): ModuleEntity {
        val moduleEntity = findById(id)
        if (moduleEntity.isEmpty) {
            throw NoSuchElementException("Module not found with id $id")
        }

        var module = moduleEntity.get()
        module.title = updateModuleDto.title
        module.description = updateModuleDto.description
        module.languageId = updateModuleDto.languageId

        return moduleRepository.save(module)
    }
}