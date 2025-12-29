package com.crosswaveconsultancy.language_server.features.userProgress

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserProgressRepository: JpaRepository<UserProgressEntity, UUID> {
    fun findAllByUserId(userId: String, sort: Sort? = null) : List<UserProgressEntity>
}