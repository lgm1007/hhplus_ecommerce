package com.example.hhplus_ecommerce.domain.statistics.dto

import com.example.hhplus_ecommerce.domain.order.dto.OrderQuantityStatisticsInfo
import com.example.hhplus_ecommerce.domain.product.dto.ProductStatisticsInfo

class OrderProductStatisticsResponseItem(
	val productId: Long,
	val name: String,
	val totalSold: Int
) {
	companion object {
		fun of(
			productStatisticsInfo: ProductStatisticsInfo,
			orderQuantityStatisticsInfo: OrderQuantityStatisticsInfo
		): OrderProductStatisticsResponseItem {
			return OrderProductStatisticsResponseItem(
				productStatisticsInfo.productId,
				productStatisticsInfo.name,
				orderQuantityStatisticsInfo.sumQuantity.toInt()
			)
		}

		fun listOf(
			productStatisticsInfos: List<ProductStatisticsInfo>,
			orderQuantityStatisticsInfos: List<OrderQuantityStatisticsInfo>
		): List<OrderProductStatisticsResponseItem> {
			return productStatisticsInfos.mapNotNull { productStatisticsInfo ->
				val orderStatisticsInfo =
					orderQuantityStatisticsInfos.find { it.productDetailId == productStatisticsInfo.productDetailId }

				orderStatisticsInfo?.let { orderStatisticsInfo ->
					of(productStatisticsInfo, orderStatisticsInfo)
				}
			}
		}
	}
}