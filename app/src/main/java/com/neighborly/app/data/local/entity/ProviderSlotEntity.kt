package com.neighborly.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "provider_slots",
    indices = [Index("providerId")]
)
data class ProviderSlotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val providerId: Long,
    val slotDate: String = "Today",
    val slotTime: String,
    val isBooked: Boolean = false,
    val isBlockedByProvider: Boolean = false
)

