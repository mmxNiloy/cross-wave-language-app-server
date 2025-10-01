package com.crosswaveconsultancy.language_server.handlers

import com.crosswaveconsultancy.language_server.exceptions.ResourceNotFoundException
import com.crosswaveconsultancy.language_server.exceptions.UnauthorizedException
import com.crosswaveconsultancy.language_server.util.ApiError
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    // 400 - Bad Request (e.g. invalid arguments)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(
        ex: MethodArgumentTypeMismatchException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.BAD_REQUEST.value(),
            message = "Invalid value for parameter '${ex.name}': ${ex.value}",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    // 400 - Constraint violation
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val errors = ex.constraintViolations.map { it.message }
        val error = ApiError(
            status = HttpStatus.BAD_REQUEST.value(),
            message = ex.message ?: "Constraint violation",
            path = request.requestURI,
            errors = errors
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    // 400 - Validation errors (@Valid DTOs)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val errors = ex.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        val error = ApiError(
            status = HttpStatus.BAD_REQUEST.value(),
            message = "Validation failed",
            path = request.requestURI,
            errors = errors
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    // 401 - Unauthorized Access
    @ExceptionHandler(UnauthorizedException::class)
    fun handleValidation(
        ex: UnauthorizedException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.UNAUTHORIZED.value(),
            message = ex.message ?: "You don't have permission to access this resource",
            path = request.requestURI,
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error)
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun handleAccessDenied(ex: org.springframework.security.access.AccessDeniedException, request: HttpServletRequest): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.UNAUTHORIZED.value(),
            message = ex.message ?: "You don't have permission to access this resource",
            path = request.requestURI,
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error)
    }

    @ExceptionHandler(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException::class)
    fun handleAuthMissing(ex: org.springframework.security.authentication.AuthenticationCredentialsNotFoundException, request: HttpServletRequest): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.UNAUTHORIZED.value(),
            message = ex.message ?: "Unauthorized! Invalid or missing authorization token.",
            path = request.requestURI,
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error)
    }

    // 404 - Resource not found
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(
        ex: ResourceNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Resource not found",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    }

    // 404 - Resource not found
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(
        ex: NoSuchElementException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Resource not found",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    }

    // 409 - Conflict (duplicate keys, etc.)
    @ExceptionHandler(IllegalStateException::class)
    fun handleConflict(
        ex: IllegalStateException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.CONFLICT.value(),
            message = ex.message ?: "Conflict occurred",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error)
    }

    // 500 - Catch-all for unhandled exceptions
    @ExceptionHandler(Exception::class)
    fun handleGeneric(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = ex.message ?: "Unexpected error occurred",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }
}