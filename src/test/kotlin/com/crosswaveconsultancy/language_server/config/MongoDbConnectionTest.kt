package com.crosswaveconsultancy.language_server.config

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MongoDbConnectionTest {
    @Autowired
    private val mongoTemplate: MongoTemplate? = null

    @Test
    fun shouldConnectToMongoDb() {
        val response = mongoTemplate?.executeCommand("{ ping: 1 }")

        assertThat(response).isNotNull()
        val document: DocumentContext = JsonPath.parse(response)
        val ok: Double? = document.read("$.ok")
        assertThat(ok).isNotNull()
        assertThat(ok).isEqualTo(1.0)
    }
}