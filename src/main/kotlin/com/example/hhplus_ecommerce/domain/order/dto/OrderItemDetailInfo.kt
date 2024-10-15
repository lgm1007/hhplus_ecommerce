package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto

class OrderItemDetailInfo(
	val productDetailId: Long,
	val quantity: Int,
	val price: Int
) {
	companion object {
		fun from(orderItemDto: OrderItemDto): OrderItemDetailInfo {
			return OrderItemDetailInfo(
				orderItemDto.productDetailId,
				orderItemDto.quantity,
				orderItemDto.price
			)
		}

		fun of(productDetail: ProductDetailDto, orderItemInfo: OrderItemInfo):OrderItemDetailInfo {
			return OrderItemDetailInfo(
				orderItemInfo.productDetailId,
				orderItemInfo.quantity,
				productDetail.price
			)
		}

		fun ofList(productDetailDtos: List<ProductDetailDto>, orderItemInfos: List<OrderItemInfo>): List<OrderItemDetailInfo> {
			return productDetailDtos.mapNotNull { productDetail ->
				val orderItemInfo = orderItemInfos.find { orderItemInfo ->
					orderItemInfo.productDetailId == productDetail.id
				}

				orderItemInfo?.let { orderItemInfo -> of(productDetail, orderItemInfo) }
			}
		}
	}
}