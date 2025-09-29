package com.crosswaveconsultancy.language_server.features.course

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface CourseRepository: JpaRepository<CourseEntity, Long> {
    fun findByModuleId(moduleId: Long, pageable: Pageable): Page<CourseEntity>
    fun findByModuleIdAndOrderIndex(moduleId: Long, orderIndex: Int): Optional<CourseEntity>
    fun existsByModuleIdAndOrderIndex(moduleId: Long, orderIndex: Int): Boolean
    fun countByModuleId(moduleId: Long): Long

    @Modifying
    @Query(
        "UPDATE CourseEntity c " +
                "SET c.orderIndex = c.orderIndex + 1 " +
                "WHERE c.module.id = :moduleId " +
                "AND c.orderIndex >= :orderIndex"
    )
    fun shiftOrderIndexes(
        @Param("moduleId") moduleId: Long,
        @Param("orderIndex") orderIndex: Int
    ): Int
}