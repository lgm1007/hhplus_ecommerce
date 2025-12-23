package com.example.hhplus_ecommerce.order.dto

import com.example.hhplus_ecommerce.product.dto.ProductDetailDto

data class OrderItemDetailInfoDto(
	val productDetailId: Long,
	val quantity: Int,
	val price: Int
) {
	fun calculateOrderPrice(): Int {
		return quantity * price;
	}

	companion object {
		fun from(orderItemDto: OrderItemDto): OrderItemDetailInfoDto {
			return OrderItemDetailInfoDto(
				orderItemDto.productDetailId,
				orderItemDto.quantity,
				orderItemDto.price
			)
		}

		fun of(productDetail: ProductDetailDto, orderItemInfoDto: OrderItemInfoDto):OrderItemDetailInfoDto {
			return OrderItemDetailInfoDto(
				orderItemInfoDto.productDetailId,
				orderItemInfoDto.quantity,
				productDetail.price
			)
		}

		fun ofList(productDetailDtos: List<ProductDetailDto>, orderItemInfoDtos: List<OrderItemInfoDto>): List<OrderItemDetailInfoDto> {
			return productDetailDtos.mapNotNull { productDetail ->
				val orderItemInfo = orderItemInfoDtos.find { orderItemInfo ->
					orderItemInfo.productDetailId == productDetail.productDetailId
				}

				orderItemInfo?.let { orderItemInfo -> of(productDetail, orderItemInfo) }
			}
		}
	}
}