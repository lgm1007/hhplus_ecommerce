package com.example.hhplus_ecommerce.order

data class OrderItemDetailInfo(
	val productDetailId: Long,
	val quantity: Int,
	val price: Int
) {
	fun calculateOrderPrice(): Int = quantity * price
}
