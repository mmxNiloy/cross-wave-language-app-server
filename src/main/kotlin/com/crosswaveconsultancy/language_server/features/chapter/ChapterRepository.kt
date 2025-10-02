package com.crosswaveconsultancy.language_server.features.chapter

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface ChapterRepository : JpaRepository<ChapterEntity, Long> {
    fun countByCourseId(courseId: Long): Long
    fun findByCourseId(courseId: Long, pageable: Pageable): Page<ChapterEntity>

    fun existsByCourseIdAndOrderIndex(courseId: Long, orderIndex: Int): Boolean
    fun findByCourseIdAndOrderIndex(courseId: Long, orderIndex: Int): Optional<ChapterEntity>

    @Modifying
    @Query(
        "UPDATE ChapterEntity c " +
                "SET c.orderIndex = c.orderIndex + 1 " +
                "WHERE c.course.id = :courseId " +
                "AND c.orderIndex >= :orderIndex"
    )
    fun shiftOrderIndexes(
        @Param("courseId") courseId: Long,
        @Param("orderIndex") orderIndex: Int,
    ): Int

    @Modifying
    @Query(
        """
    UPDATE ChapterEntity c
    SET c.orderIndex = c.orderIndex + 1
    WHERE c.course.id = :courseId
      AND c.orderIndex >= :newIndex
      AND c.orderIndex < :oldIndex
"""
    )
    fun shiftUpIndexes(
        @Param("courseId") courseId: Long,
        @Param("newIndex") newIndex: Int,
        @Param("oldIndex") oldIndex: Int
    ): Int

    @Modifying
    @Query(
        """
    UPDATE ChapterEntity c
    SET c.orderIndex = c.orderIndex - 1
    WHERE c.course.id = :courseId
      AND c.orderIndex > :oldIndex
      AND c.orderIndex <= :newIndex
"""
    )
    fun shiftDownIndexes(
        @Param("courseId") courseId: Long,
        @Param("oldIndex") oldIndex: Int,
        @Param("newIndex") newIndex: Int
    ): Int

    fun countByCourse_LanguageCode(languageCode: String): Long
}