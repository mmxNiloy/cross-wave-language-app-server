package com.crosswaveconsultancy.language_server.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value= HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String): RuntimeException(message) {

}