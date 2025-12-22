package com.example.hhplus_ecommerce.share.exception

data class NotFoundException(val errorStatus: ErrorStatus) : RuntimeException() {
}