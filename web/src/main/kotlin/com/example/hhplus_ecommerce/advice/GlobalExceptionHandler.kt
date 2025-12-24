package com.example.hhplus_ecommerce.advice

import com.example.hhplus_ecommerce.share.exception.BadRequestException
import com.example.hhplus_ecommerce.share.exception.ExceptionBody
import com.example.hhplus_ecommerce.share.exception.NotFoundException
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
	fun handleBadRequestException(e: BadRequestException): ResponseEntity<ExceptionBody> {
		logger.error(e.errorStatus.message, e)
		return ResponseEntity(ExceptionBody(e.errorStatus.message, 400), HttpStatus.BAD_REQUEST)
	}

	/**
	 * NotFound 예외 처리
	 * code: 404
	 * status: NOT_FOUND
	 */
	@ExceptionHandler(NotFoundException::class)
	fun handleNotFoundException(e: NotFoundException): ResponseEntity<ExceptionBody> {
		logger.error(e.errorStatus.message, e)
		return ResponseEntity(ExceptionBody(e.errorStatus.message, 404), HttpStatus.NOT_FOUND)
	}
}