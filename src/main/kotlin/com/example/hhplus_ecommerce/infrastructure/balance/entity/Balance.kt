package com.example.hhplus_ecommerce.infrastructure.balance.entity

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.exception.BadRequestException
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Balance(
	val userId: Long,
	var amount: Int,
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

	fun decreaseAmount(amount: Int) {
		if (this.amount - amount < 0) {
			throw BadRequestException(ErrorStatus.NOT_ENOUGH_BALANCE)
		}

		this.amount -= amount
	}

	companion object {
		fun from(balanceDto: BalanceDto): Balance {
			return Balance(
				balanceDto.userId,
				balanceDto.amount
			)
		}
	}
}