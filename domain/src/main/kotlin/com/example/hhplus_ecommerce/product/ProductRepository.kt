package com.example.hhplus_ecommerce.product

interface ProductRepository {
	fun insert(product: Product): Product

	fun getAllByPaging(page: Int, itemSize: Int): List<Product>

	fun getById(id: Long): Product

	fun getAllByIds(ids: List<Long>): List<Product>
}