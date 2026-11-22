package com.neighborly.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.neighborly.app.data.local.entity.BookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE customerPhone = :customerPhone ORDER BY createdAt DESC")
    fun getBookingsForCustomer(customerPhone: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE providerId = :providerId ORDER BY createdAt DESC")
    fun getBookingsForProvider(providerId: Long): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    fun getBookingById(id: Long): Flow<BookingEntity?>

    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingByIdDirect(id: Long): BookingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("UPDATE bookings SET revisedQuoteInr = :revisedQuote, revisedQuoteReason = :reason, isRevisedQuoteApproved = 0 WHERE id = :id")
    suspend fun proposeRevisedQuote(id: Long, revisedQuote: Int, reason: String)

    @Query("UPDATE bookings SET isRevisedQuoteApproved = 1 WHERE id = :id")
    suspend fun approveRevisedQuote(id: Long)

    @Query("UPDATE bookings SET status = 'Completed', finalPaidAmountInr = :finalAmount WHERE id = :id")
    suspend fun completeBooking(id: Long, finalAmount: Int)

    @Query("UPDATE bookings SET rating = :rating, reviewText = :reviewText, isReviewed = 1 WHERE id = :id")
    suspend fun submitRating(id: Long, rating: Float, reviewText: String)
}

