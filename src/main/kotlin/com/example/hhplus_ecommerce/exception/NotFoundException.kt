package com.example.hhplus_ecommerce.exception

import com.example.hhplus_ecommerce.api.error.ErrorStatus

class NotFoundException(val errorStatus: ErrorStatus) : RuntimeException() {
}