package com.example.calandfast.database

import kotlinx.coroutines.flow.Flow

interface ConsumablesRepository {
    
    /**
     * Retrieve all the Consumables from the the given data source.
     */
    fun getAllConsumablesStream(): Flow<List<Consumable>>

    /**
     * Retrieve an Consumable from the given data source that matches with the [id].
     */
    fun getConsumableStream(id: Int): Flow<Consumable?>

    /**
     * Insert Consumable in the data source
     */
    suspend fun insertConsumable(consumable: Consumable)

    /**
     * Delete Consumable from the data source
     */
    suspend fun deleteConsumable(consumable: Consumable)

    /**
     * Update Consumable in the data source
     */
    suspend fun updateConsumable(consumable: Consumable)
    
    suspend fun upsertConsumable(consumable: Consumable)
}