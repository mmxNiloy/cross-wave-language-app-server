package com.crosswaveconsultancy.language_server.features.course

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CourseRepository : JpaRepository<CourseEntity, Long> {
    fun findByLanguageCode(languageCode: String, pageable: Pageable): Page<CourseEntity>
    fun existsByLanguageCodeAndOrderIndex(languageCode: String, orderIndex: Int): Boolean
    fun countByLanguageCode(languageCode: String): Long

    @Modifying
    @Query(
        "UPDATE CourseEntity c " +
                "SET c.orderIndex = c.orderIndex + 1 " +
                "WHERE c.language.shortCode = :languageCode " +
                "AND c.orderIndex >= :orderIndex"
    )
    fun shiftOrderIndexes(
        @Param("languageCode") languageCode: String,
        @Param("orderIndex") orderIndex: Int
    ): Int

    @Modifying
    @Query(
        """
    UPDATE CourseEntity c
    SET c.orderIndex = c.orderIndex + 1
    WHERE c.language.shortCode = :languageCode
      AND c.orderIndex >= :newIndex
      AND c.orderIndex < :oldIndex
"""
    )
    fun shiftUpIndexes(
        @Param("languageCode") languageCode: String,
        @Param("newIndex") newIndex: Int,
        @Param("oldIndex") oldIndex: Int
    ): Int

    @Modifying
    @Query(
        """
    UPDATE CourseEntity c
    SET c.orderIndex = c.orderIndex - 1
    WHERE c.language.shortCode = :languageCode
      AND c.orderIndex > :oldIndex
      AND c.orderIndex <= :newIndex
"""
    )
    fun shiftDownIndexes(
        @Param("languageCode") languageCode: String,
        @Param("oldIndex") oldIndex: Int,
        @Param("newIndex") newIndex: Int
    ): Int
}