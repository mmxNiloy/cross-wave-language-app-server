package com.crosswaveconsultancy.language_server.features.lesson

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LessonRepository : JpaRepository<LessonEntity, Long> {
    fun findByChapterId(chapterId: Long, pageable: Pageable): Page<LessonEntity>
    fun countByChapterId(chapterId: Long): Long

    fun existsByChapterIdAndOrderIndex(chapterId: Long, orderIndex: Int): Boolean
    fun findByChapterIdAndOrderIndex(chapterId: Long, orderIndex: Int): LessonEntity?

    @Modifying
    @Query(
        "UPDATE LessonEntity l " +
                "SET l.orderIndex = l.orderIndex + 1 " +
                "WHERE l.chapter.id = :chapterId " +
                "AND l.orderIndex >= :orderIndex"
    )
    fun shiftOrderIndexes(
        @Param("chapterId") chapterId: Long,
        @Param("orderIndex") orderIndex: Int
    ): Int

    @Modifying
    @Query(
        """
    UPDATE LessonEntity c
    SET c.orderIndex = c.orderIndex + 1
    WHERE c.chapter.id = :chapterId
      AND c.orderIndex >= :newIndex
      AND c.orderIndex < :oldIndex
"""
    )
    fun shiftUpIndexes(
        @Param("chapterId") chapterId: Long,
        @Param("newIndex") newIndex: Int,
        @Param("oldIndex") oldIndex: Int
    ): Int

    @Modifying
    @Query(
        """
    UPDATE LessonEntity c
    SET c.orderIndex = c.orderIndex - 1
    WHERE c.chapter.id = :chapterId
      AND c.orderIndex > :oldIndex
      AND c.orderIndex <= :newIndex
"""
    )
    fun shiftDownIndexes(
        @Param("chapterId") chapterId: Long,
        @Param("oldIndex") oldIndex: Int,
        @Param("newIndex") newIndex: Int
    ): Int

    fun countByChapter_Course_LanguageCode(languageCode: String): Long
}