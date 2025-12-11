package com.example.hhplus_ecommerce.domain.share.exception

import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus

data class NotFoundException(val errorStatus: ErrorStatus) : RuntimeException() {
}