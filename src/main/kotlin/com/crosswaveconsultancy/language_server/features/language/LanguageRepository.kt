package com.crosswaveconsultancy.language_server.features.language

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LanguageRepository : JpaRepository<LanguageEntity, Long>