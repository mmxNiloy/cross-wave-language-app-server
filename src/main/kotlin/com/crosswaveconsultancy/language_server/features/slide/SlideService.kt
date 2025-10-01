package com.crosswaveconsultancy.language_server.features.slide

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.features.slide.document.LessonCounterDocument
import com.crosswaveconsultancy.language_server.features.slide.document.SlideDocument
import com.crosswaveconsultancy.language_server.features.slide.dto.CreateSlideDto
import com.crosswaveconsultancy.language_server.features.slide.dto.UpdateSlideDto
import com.crosswaveconsultancy.language_server.features.slide.repository.LessonCounterRepository
import com.crosswaveconsultancy.language_server.features.slide.repository.SlideRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SlideService(
    private val slideRepository: SlideRepository,
    private val mongoTemplate: MongoTemplate
) {
    fun getSlides(page: Int, limit: Int): List<SlideDocument> {
        val pageRequest = PageRequest.of(page - 1, limit)
        return slideRepository.findByIsActive(pageRequest).toList()
    }

    fun count(): Long {
        return slideRepository.countByIsActive()
    }

    fun countByLessonId(lessonId: Long): Long {
        return slideRepository.countByLessonIdAndIsActive(lessonId)
    }

    fun getSlidesByLessonId(lessonId: Long, page: Int, limit: Int): List<SlideDocument> {
        val pageRequest = PageRequest.of(page - 1, limit)
        return slideRepository.findByLessonIdAndIsActive(pageRequest, lessonId).toList()
    }

    fun getSlideById(id: String): SlideDocument {
        return slideRepository.findByIdAndIsActive(id).orElseThrow { ResourceNotFoundException("Slide not found with id $id") }
    }

    fun createSlide(slideDto: CreateSlideDto): SlideDocument {
        val nextIndex = getNextOrderIndex(slideDto.lessonId)
        val slideDocument = slideDto.toDocument(nextIndex)
        return slideRepository.save(slideDocument)
    }

    private fun getNextOrderIndex(lessonId: Long): Int {
        val query = Query(Criteria.where("_id").`is`(lessonId))
        val update = Update().inc("count", 1)
        val options = FindAndModifyOptions.options()
            .returnNew(true)   // return the incremented value
            .upsert(true)      // create if not exists

        val counter = mongoTemplate.findAndModify(
            query, update, options, LessonCounterDocument::class.java
        )

        return counter!!.count
    }

    private fun updateInactiveCount(lessonId: Long, isActive: Boolean): Int {
        val query = Query(Criteria.where("_id").`is`(lessonId))
        val update = Update().inc("inactiveCount", if (isActive) -1 else 1)
        val options = FindAndModifyOptions.options()
            .returnNew(true)   // return the incremented value
            .upsert(true)      // create if not exists

        val counter = mongoTemplate.findAndModify(
            query, update, options, LessonCounterDocument::class.java
        )

        return counter!!.inactiveCount
    }

    @Transactional
    fun updateSlide(id: String, dto: UpdateSlideDto): SlideDocument {
        val slide = slideRepository.findByIdAndIsActive(id)
            .orElseThrow { ResourceNotFoundException("Slide not found with id $id") }

        // Update title, lessonId, sections if present
        dto.title?.let { slide.title = it }
        dto.sections?.let { slide.sections = it.map { it.toDocument() } }

        // Handle reordering if orderIndex is specified
        dto.orderIndex?.let { newIndex ->
            if (newIndex != slide.orderIndex) {
                reorderSlide(slide, newIndex) // reuse reordering logic
            }
        }

        return slideRepository.save(slide)
    }

    @Transactional
    fun toggleSlide(id: String, isActive: Boolean): SlideDocument {
        var slide = getSlideById(id)
        slide.isActive = isActive
        updateInactiveCount(slide.lessonId, isActive)
        return slideRepository.save(slide)
    }


    private fun reorderSlide(slide: SlideDocument, newIndex: Int) {
        val lessonId = slide.lessonId
        val oldIndex = slide.orderIndex
        val totalSlides = slideRepository.countByLessonIdAndIsActive(lessonId).toInt()

        if (newIndex < 1 || newIndex > totalSlides) {
            throw IllegalArgumentException("orderIndex must be between 1 and $totalSlides")
        }

        if (oldIndex < newIndex) {
            val q = Query(
                Criteria.where("lessonId").`is`(lessonId)
                    .and("orderIndex").gt(oldIndex).lte(newIndex)
            )
            val update = Update().inc("orderIndex", -1)
            mongoTemplate.updateMulti(q, update, SlideDocument::class.java)
        } else {
            val q = Query(
                Criteria.where("lessonId").`is`(lessonId)
                    .and("orderIndex").gte(newIndex).lt(oldIndex)
            )
            val update = Update().inc("orderIndex", 1)
            mongoTemplate.updateMulti(q, update, SlideDocument::class.java)
        }

        // Finally set the moved slide’s order
        slide.orderIndex = newIndex
    }
}