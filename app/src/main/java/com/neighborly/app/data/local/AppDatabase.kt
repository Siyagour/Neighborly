package com.neighborly.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.neighborly.app.data.local.dao.BookingDao
import com.neighborly.app.data.local.dao.ProviderDao
import com.neighborly.app.data.local.dao.ReviewDao
import com.neighborly.app.data.local.dao.SlotDao
import com.neighborly.app.data.local.dao.UserAccountDao
import com.neighborly.app.data.local.entity.BookingEntity
import com.neighborly.app.data.local.entity.ProviderEntity
import com.neighborly.app.data.local.entity.ProviderSlotEntity
import com.neighborly.app.data.local.entity.ReviewEntity
import com.neighborly.app.data.local.entity.SubServiceEntity
import com.neighborly.app.data.local.entity.UserAccountEntity

@Database(
    entities = [
        UserAccountEntity::class,
        ProviderEntity::class,
        SubServiceEntity::class,
        ProviderSlotEntity::class,
        BookingEntity::class,
        ReviewEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userAccountDao(): UserAccountDao
    abstract fun providerDao(): ProviderDao
    abstract fun slotDao(): SlotDao
    abstract fun bookingDao(): BookingDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "neighborly_marketplace.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
