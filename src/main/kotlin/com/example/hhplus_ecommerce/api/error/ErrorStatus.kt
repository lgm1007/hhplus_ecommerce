package com.example.hhplus_ecommerce.api.error

enum class ErrorStatus(val message: String) {
	CHARGED_AMOUNT_ERROR("충전 금액 에러"),
	NOT_FOUND_USER("사용자 없음"),
	NOT_FOUND_USER_BALANCE("사용자에 대한 비용 정보 없음"),
	NOT_FOUND_PRODUCT("상품 없음"),
	NOT_FOUND_ORDER("주문 정보 없음"),
	NOT_FOUND_CART("장바구니 없음"),
	NOT_ENOUGH_BALANCE("잔액 부족"),
	NOT_ENOUGH_QUANTITY("재고 부족")
}