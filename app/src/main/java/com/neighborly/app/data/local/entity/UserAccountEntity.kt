package com.neighborly.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_accounts",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["phone"], unique = true)
    ]
)
data class UserAccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val role: String, // "USER" or "PROVIDER"
    val address: String,
    val providerId: Long? = null
)

