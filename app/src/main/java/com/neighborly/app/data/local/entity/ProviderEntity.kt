package com.neighborly.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "providers")
data class ProviderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val initials: String,
    val avatarColorHex: String,
    val rating: Float,
    val reviewCount: Int,
    val distanceKm: Float,
    val responseTimeMins: Int,
    val isVerified: Boolean = true,
    val isOnline: Boolean = true,
    val experienceYears: Int,
    val phone: String,
    val area: String,
    val bio: String,
    val visitingFeeInr: Int
)

