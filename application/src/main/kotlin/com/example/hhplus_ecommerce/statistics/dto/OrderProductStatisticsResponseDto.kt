package com.example.hhplus_ecommerce.statistics.dto

import com.example.hhplus_ecommerce.order.dto.OrderQuantityStatisticsInfoDto
import com.example.hhplus_ecommerce.product.dto.ProductStatisticsInfoDto
import java.io.Serializable

class OrderProductStatisticsResponseDto(
	val productId: Long,
	val name: String,
	val totalSold: Int
) : Serializable {
	companion object {
		fun of(
			productStatisticsInfoDto: ProductStatisticsInfoDto,
			orderQuantityStatisticsInfoDto: OrderQuantityStatisticsInfoDto
		): OrderProductStatisticsResponseDto {
			return OrderProductStatisticsResponseDto(
				productId = productStatisticsInfoDto.productId,
				name = productStatisticsInfoDto.name,
				totalSold = orderQuantityStatisticsInfoDto.sumQuantity.toInt()
			)
		}

		fun listOf(
			productStatisticsInfoDtos: List<ProductStatisticsInfoDto>,
			orderQuantityStatisticsInfoDtos: List<OrderQuantityStatisticsInfoDto>
		): List<OrderProductStatisticsResponseDto> {
			return productStatisticsInfoDtos.mapNotNull { productStatisticsInfo ->
				val orderStatisticsInfo =
					orderQuantityStatisticsInfoDtos.find { it.productDetailId == productStatisticsInfo.productDetailId }

				orderStatisticsInfo?.let { orderStatisticsInfo ->
					of(productStatisticsInfo, orderStatisticsInfo)
				}
			}
		}
	}
}