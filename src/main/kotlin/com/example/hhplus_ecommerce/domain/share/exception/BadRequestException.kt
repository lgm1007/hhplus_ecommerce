package com.example.hhplus_ecommerce.domain.share.exception

import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus

data class BadRequestException(val errorStatus: ErrorStatus) : RuntimeException() {
}