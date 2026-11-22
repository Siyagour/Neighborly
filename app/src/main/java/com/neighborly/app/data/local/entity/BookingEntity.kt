package com.neighborly.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookings",
    indices = [Index("providerId"), Index("slotId")]
)
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val providerId: Long,
    val slotId: Long,
    val customerName: String,
    val customerPhone: String,
    val subServiceName: String,
    val slotTime: String,
    val status: String, // Requested, Accepted, In Progress, Completed, Declined, Cancelled
    val issueNote: String = "",
    val startOtp: String,
    val initialQuoteMinInr: Int,
    val initialQuoteMaxInr: Int,
    val revisedQuoteInr: Int? = null,
    val revisedQuoteReason: String? = null,
    val isRevisedQuoteApproved: Boolean = false,
    val finalPaidAmountInr: Int? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val rating: Float? = null,
    val reviewText: String? = null,
    val isReviewed: Boolean = false
)

