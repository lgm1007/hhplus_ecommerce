package com.example.hhplus_ecommerce.api.advice

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.exception.NotFoundException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
	private val logger = KotlinLogging.logger {}

	/**
	 * BadRequest 예외 처리
	 * code: 400
	 * status: BAD_REQUEST
	 */
	@ExceptionHandler(BadRequestException::class)
	fun handleBadRequestException(e: BadRequestException): ResponseEntity<ErrorBody> {
		logger.error(e.errorStatus.message, e)
		return ResponseEntity(ErrorBody(e.errorStatus.message, 400), HttpStatus.BAD_REQUEST)
	}

	/**
	 * NotFound 예외 처리
	 * code: 404
	 * status: NOT_FOUND
	 */
	@ExceptionHandler(NotFoundException::class)
	fun handleNotFoundException(e: NotFoundException): ResponseEntity<ErrorBody> {
		logger.error(e.errorStatus.message, e)
		return ResponseEntity(ErrorBody(e.errorStatus.message, 404), HttpStatus.NOT_FOUND)
	}
}