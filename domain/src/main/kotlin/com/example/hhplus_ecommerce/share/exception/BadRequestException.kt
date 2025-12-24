package com.example.hhplus_ecommerce.share.exception

data class BadRequestException(val errorStatus: ErrorStatus) : RuntimeException() {
}