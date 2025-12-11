package com.example.hhplus_ecommerce.infrastructure.balance.entity

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceHistoryDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "BALANCEHISTORY")
class BalanceHistoryEntity(
	val balanceId: Long,
	val userId: Long,
	val updateAmount: Int
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0

	@CreatedDate
	var createdDate: LocalDateTime = LocalDateTime.now()
		private set

	companion object {
		fun from(balanceHistoryDto: BalanceHistoryDto): BalanceHistoryEntity {
			return BalanceHistoryEntity(
				balanceHistoryDto.balanceId,
				balanceHistoryDto.userId,
				balanceHistoryDto.updateAmount
			)
		}
	}
}