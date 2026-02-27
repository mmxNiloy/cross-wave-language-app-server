package com.crosswaveconsultancy.language_server.features.kanji

import org.springframework.data.jpa.repository.JpaRepository

interface KanjiRepository : JpaRepository<KanjiEntity, Long> {
}