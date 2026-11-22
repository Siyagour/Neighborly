package com.neighborly.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.neighborly.app.data.local.entity.UserAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAccountDao {
    @Query("SELECT * FROM user_accounts WHERE email = :identifier OR phone = :identifier LIMIT 1")
    suspend fun getUserByEmailOrPhone(identifier: String): UserAccountEntity?

    @Query("SELECT * FROM user_accounts WHERE id = :id")
    fun getUserById(id: Long): Flow<UserAccountEntity?>

    @Query("SELECT * FROM user_accounts WHERE id = :id")
    suspend fun getUserByIdDirect(id: Long): UserAccountEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserAccountEntity): Long

    @Update
    suspend fun updateUser(user: UserAccountEntity)

    @Query("SELECT COUNT(*) FROM user_accounts")
    suspend fun getUserCount(): Int
}

