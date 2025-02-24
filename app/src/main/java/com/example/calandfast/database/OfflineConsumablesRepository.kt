package com.example.calandfast.database

import kotlinx.coroutines.flow.Flow

class OfflineConsumablesRepository(private val consumableDao: ConsumableDao) : ConsumablesRepository {

    override fun getAllConsumablesStream(): Flow<List<Consumable>> = consumableDao.getAllItems()

    override fun getConsumableStream(id: Int): Flow<Consumable?> = consumableDao.getItem(id)

    override suspend fun insertConsumable(consumable: Consumable) = consumableDao.upsertConsumable(consumable)

    override suspend fun deleteConsumable(consumable: Consumable) = consumableDao.deleteConsumable(consumable)

    override suspend fun updateConsumable(consumable: Consumable) = consumableDao.updateConsumable(consumable)

    override suspend fun upsertConsumable(consumable: Consumable) =consumableDao.upsertConsumable(consumable)
}