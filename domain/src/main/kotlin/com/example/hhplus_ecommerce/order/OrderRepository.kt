package com.example.hhplus_ecommerce.order

interface OrderRepository {
	fun insert(order: Order): Order

	fun getById(orderId: Long): Order

	fun updateOrderStatus(orderId: Long, orderStatus: OrderStatus): Order
}