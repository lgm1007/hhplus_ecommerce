package com.example.hhplus_ecommerce.exception

import com.example.hhplus_ecommerce.api.error.ErrorStatus

data class BadRequestException(val errorStatus: ErrorStatus) : RuntimeException() {
}