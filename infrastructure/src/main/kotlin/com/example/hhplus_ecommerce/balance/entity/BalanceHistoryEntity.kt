package com.example.hhplus_ecommerce.balance.entity

import com.example.hhplus_ecommerce.BaseEntity
import com.example.hhplus_ecommerce.balance.BalanceHistory
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "BALANCEHISTORY")
class BalanceHistoryEntity(
	val balanceId: Long,
	val userId: Long,
	val updateAmount: Int
) : BaseEntity() {
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