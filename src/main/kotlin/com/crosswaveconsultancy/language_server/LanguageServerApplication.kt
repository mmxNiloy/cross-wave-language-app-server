package com.crosswaveconsultancy.language_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class LanguageServerApplication

fun main(args: Array<String>) {
	runApplication<LanguageServerApplication>(*args)
}
