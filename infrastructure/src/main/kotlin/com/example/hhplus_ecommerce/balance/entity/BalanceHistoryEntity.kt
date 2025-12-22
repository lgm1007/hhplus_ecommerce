package com.example.hhplus_ecommerce.balance.entity

import com.example.hhplus_ecommerce.balance.BalanceHistory
import org.springframework.data.annotation.CreatedDate
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
		fun from(balanceHistory: BalanceHistory): BalanceHistoryEntity {
			return BalanceHistoryEntity(
				balanceId = balanceHistory.balanceId,
				userId = balanceHistory.userId,
				updateAmount = balanceHistory.updateAmount
			)
		}
	}

	fun toDomain(): BalanceHistory {
		return BalanceHistory(
			balanceHistoryId = this.id,
			balanceId = this.balanceId,
			userId = this.userId,
			updateAmount = this.updateAmount,
			createdDate = this.createdDate
		)
	}
}