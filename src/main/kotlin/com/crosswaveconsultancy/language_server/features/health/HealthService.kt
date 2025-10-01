package com.crosswaveconsultancy.language_server.features.health

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import javax.sql.DataSource

@Service
class HealthService(
    private val dataSource: DataSource,
    private val mongoTemplate: MongoTemplate
) {
    fun isMongoDBUp(): Boolean {
        val response = mongoTemplate.executeCommand("{ ping: 1 }")
        return response.getDouble("ok") == 1.0
    }

    fun isDatabaseUp(): Boolean {
        return dataSource.connection.use {connection -> connection.isValid(1)}
    }
}