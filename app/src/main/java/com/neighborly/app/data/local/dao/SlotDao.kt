package com.neighborly.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neighborly.app.data.local.entity.ProviderSlotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SlotDao {
    @Query("SELECT * FROM provider_slots WHERE providerId = :providerId ORDER BY id ASC")
    fun getSlotsForProvider(providerId: Long): Flow<List<ProviderSlotEntity>>

    @Query("SELECT * FROM provider_slots WHERE providerId = :providerId AND isBooked = 0 AND isBlockedByProvider = 0 ORDER BY id ASC")
    fun getAvailableSlotsForProvider(providerId: Long): Flow<List<ProviderSlotEntity>>

    @Query("SELECT * FROM provider_slots WHERE id = :slotId")
    suspend fun getSlotById(slotId: Long): ProviderSlotEntity?

    @Query("UPDATE provider_slots SET isBlockedByProvider = :isBlocked WHERE id = :slotId")
    suspend fun updateBlockedStatus(slotId: Long, isBlocked: Boolean)

    @Query("UPDATE provider_slots SET isBooked = :isBooked WHERE id = :slotId")
    suspend fun updateBookedStatus(slotId: Long, isBooked: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlots(slots: List<ProviderSlotEntity>)
}

