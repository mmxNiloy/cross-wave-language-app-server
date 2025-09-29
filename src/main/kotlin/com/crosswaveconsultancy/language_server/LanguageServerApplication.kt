package com.crosswaveconsultancy.language_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LanguageServerApplication

fun main(args: Array<String>) {
	runApplication<LanguageServerApplication>(*args)
}
