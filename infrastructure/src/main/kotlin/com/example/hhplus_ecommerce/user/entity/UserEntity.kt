package com.example.hhplus_ecommerce.user.entity

import com.example.hhplus_ecommerce.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "USERTABLE")
class UserEntity(
	val name: String,
	val email: String
) : BaseEntity()