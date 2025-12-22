package com.example.hhplus_ecommerce.balance.entity

import com.example.hhplus_ecommerce.balance.Balance
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table
import javax.persistence.Version

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "BALANCE", indexes = [Index(name = "idx_balance_user_id", columnList = "userId")])
class BalanceEntity(
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