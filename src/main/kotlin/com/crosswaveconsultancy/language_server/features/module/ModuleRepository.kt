package com.crosswaveconsultancy.language_server.features.module

import org.springframework.data.jpa.repository.JpaRepository

interface ModuleRepository: JpaRepository<ModuleEntity, Long> {
//    fun softDeleteById(id: Long) {
//        findById(id).ifPresent {
//            it.isActive = false
//            save(it)
//        }
//    }
}