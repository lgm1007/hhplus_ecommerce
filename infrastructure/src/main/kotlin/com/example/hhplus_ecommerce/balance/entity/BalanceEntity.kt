package com.example.hhplus_ecommerce.balance.entity

import com.example.hhplus_ecommerce.BaseEntity
import com.example.hhplus_ecommerce.balance.Balance
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table(name = "BALANCE", indexes = [Index(name = "idx_balance_user_id", columnList = "userId")])
class BalanceEntity(
	val userId: Long,
	var amount: Int,
) : BaseEntity() {
	@LastModifiedDate
	var lastModifiedDate: LocalDateTime = LocalDateTime.now()
		private set

	@Version
	var version: Long = 0

	companion object {
		fun from(balance: Balance): BalanceEntity {
			return BalanceEntity(
				userId = balance.userId,
				amount = balance.amount
			)
		}
	}

	fun toDomain(): Balance {
		return Balance(
			balanceId = this.id,
			userId = this.userId,
			amount = this.amount,
			createdDate = this.createdDate,
			lastModifiedDate = this.lastModifiedDate
		)
	}
}