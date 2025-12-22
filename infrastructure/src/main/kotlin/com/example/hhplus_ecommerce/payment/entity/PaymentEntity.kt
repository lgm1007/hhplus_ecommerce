package com.example.hhplus_ecommerce.payment.entity

import com.example.hhplus_ecommerce.payment.Payment
import com.example.hhplus_ecommerce.payment.PaymentStatus
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "PAYMENT")
class PaymentEntity(
	val userId: Long,
	val orderId: Long,
	val price: Int,
	val paymentStatus: PaymentStatus
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0

	@CreatedDate
	var createdDate: LocalDateTime = LocalDateTime.now()
		private set

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