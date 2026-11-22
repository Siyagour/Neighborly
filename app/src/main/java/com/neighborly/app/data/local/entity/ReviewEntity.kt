package com.neighborly.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    indices = [Index("providerId")]
)
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val providerId: Long,
    val customerName: String,
    val rating: Float,
    val comment: String,
    val dateStr: String
)

