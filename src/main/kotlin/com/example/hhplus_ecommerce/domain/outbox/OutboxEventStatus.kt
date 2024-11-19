package com.example.hhplus_ecommerce.domain.outbox

enum class OutboxEventStatus(val message: String) {
	INIT("등록"),
	PUBLISH("발행"),
	COMPLETE("성공");
}