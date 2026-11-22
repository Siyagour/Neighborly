package com.neighborly.app

import android.app.Application
import com.neighborly.app.data.local.AppDatabase
import com.neighborly.app.data.repository.MarketplaceRepository
import com.neighborly.app.data.seed.DatabaseSeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NeighborlyApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { MarketplaceRepository(database) }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            DatabaseSeedData.seedDatabaseIfEmpty(database)
        }
    }
}

