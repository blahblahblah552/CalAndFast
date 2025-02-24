package com.example.calandfast.database

import android.content.Context

interface AppContainer {
    val consumablesRepository: ConsumablesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val consumablesRepository: ConsumablesRepository by lazy {
        OfflineConsumablesRepository(ConsumableDatabase.getDatabase(context).consumableDao())
    }
}
