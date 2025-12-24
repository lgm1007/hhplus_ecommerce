package com.example.hhplus_ecommerce.order

interface OrderItemRepository {
	fun insert(orderItem: OrderItem): OrderItem

	fun insertAll(orderItems: List<OrderItem>): List<OrderItem>

	fun getAllOrderItemsTopMoreThanDay(day: Int, limit: Int): List<OrderQuantityStatistics>
}