package com.example.hhplus_ecommerce.infrastructure.payment.entity

import com.example.hhplus_ecommerce.domain.payment.PaymentStatus
import com.example.hhplus_ecommerce.domain.payment.dto.PaymentDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

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
		fun from(paymentDto: PaymentDto): PaymentEntity {
			return PaymentEntity(
				paymentDto.userId,
				paymentDto.orderId,
				paymentDto.price,
				paymentDto.paymentStatus
			)
		}
	}
}