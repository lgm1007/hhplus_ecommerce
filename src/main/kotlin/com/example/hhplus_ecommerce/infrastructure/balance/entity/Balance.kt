package com.example.hhplus_ecommerce.infrastructure.balance.entity

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Balance(
	val userId: Long,
	val amount: Int,
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
		fun from(balanceDto: BalanceDto): Balance {
			return Balance(
				balanceDto.userId,
				balanceDto.amount
			)
		}
	}
}