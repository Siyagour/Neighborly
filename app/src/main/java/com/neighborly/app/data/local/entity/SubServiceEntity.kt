package com.neighborly.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sub_services",
    indices = [Index("providerId")]
)
data class SubServiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val providerId: Long,
    val serviceName: String,
    val minPriceInr: Int,
    val maxPriceInr: Int
)

