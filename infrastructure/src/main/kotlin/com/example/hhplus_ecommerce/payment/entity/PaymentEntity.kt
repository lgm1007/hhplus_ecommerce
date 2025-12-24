package com.example.hhplus_ecommerce.payment.entity

import com.example.hhplus_ecommerce.BaseEntity
import com.example.hhplus_ecommerce.payment.Payment
import com.example.hhplus_ecommerce.payment.PaymentStatus
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "PAYMENT")
class PaymentEntity(
	val userId: Long,
	val orderId: Long,
	val price: Int,
	val paymentStatus: PaymentStatus
) : BaseEntity() {
	@LastModifiedDate
	var lastModifiedDate: LocalDateTime = LocalDateTime.now()
		private set

	companion object {
		fun from(payment: Payment): PaymentEntity {
			return PaymentEntity(
				userId = payment.userId,
				orderId = payment.orderId,
				price = payment.price,
				paymentStatus = payment.paymentStatus
			)
		}
	}

	fun toDomain(): Payment {
		return Payment(
			paymentId = this.id,
			userId = this.userId,
			orderId = this.orderId,
			price = this.price,
			paymentStatus = this.paymentStatus,
			createdDate = this.createdDate
		)
	}
}