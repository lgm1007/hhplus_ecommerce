package com.example.hhplus_ecommerce.domain.outbox

enum class OutboxEventStatus(val message: String) {
	BEFORE_PROCESS("처리 전"),
	COMPLETE("성공");
}