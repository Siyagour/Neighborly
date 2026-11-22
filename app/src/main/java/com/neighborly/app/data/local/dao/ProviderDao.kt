package com.neighborly.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.neighborly.app.data.local.entity.ProviderEntity
import com.neighborly.app.data.local.entity.SubServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderDao {
    @Query("SELECT * FROM providers ORDER BY rating DESC")
    fun getAllProviders(): Flow<List<ProviderEntity>>

    @Query("SELECT * FROM providers WHERE id = :id")
    fun getProviderById(id: Long): Flow<ProviderEntity?>

    @Query("SELECT * FROM providers WHERE id = :id")
    suspend fun getProviderByIdDirect(id: Long): ProviderEntity?

    @Query("UPDATE providers SET isOnline = :isOnline WHERE id = :id")
    suspend fun updateOnlineStatus(id: Long, isOnline: Boolean)

    @Query("UPDATE providers SET rating = :rating, reviewCount = :reviewCount WHERE id = :id")
    suspend fun updateRating(id: Long, rating: Float, reviewCount: Int)

    @Query("UPDATE providers SET name = :name, phone = :phone, bio = :bio, visitingFeeInr = :visitingFee, experienceYears = :experience, category = :category WHERE id = :id")
    suspend fun updateProviderProfile(
        id: Long,
        name: String,
        phone: String,
        bio: String,
        visitingFee: Int,
        experience: Int,
        category: String
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(provider: ProviderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProviders(providers: List<ProviderEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubServices(services: List<SubServiceEntity>)

    @Query("SELECT * FROM sub_services WHERE providerId = :providerId")
    fun getSubServices(providerId: Long): Flow<List<SubServiceEntity>>

    @Query("SELECT * FROM sub_services WHERE providerId = :providerId")
    suspend fun getSubServicesDirect(providerId: Long): List<SubServiceEntity>

    @Query("SELECT COUNT(*) FROM providers")
    suspend fun getProviderCount(): Int
}
