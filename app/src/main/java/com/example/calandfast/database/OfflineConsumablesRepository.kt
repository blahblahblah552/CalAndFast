package com.example.calandfast.database

import kotlinx.coroutines.flow.Flow

class OfflineConsumablesRepository(private val consumableDao: ConsumableDao) : ConsumablesRepository {

    override fun getAllConsumablesStream(): Flow<List<Consumable>> = consumableDao.getAllItemsByLastUsed()

    override fun getConsumableStream(id: Int): Flow<Consumable?> = consumableDao.getItem(id)

    override suspend fun insertConsumable(consumable: Consumable) = consumableDao.upsertConsumable(consumable)

    override suspend fun deleteConsumable(consumable: Consumable) = consumableDao.deleteConsumable(consumable)

    override suspend fun updateConsumable(consumable: Consumable) = consumableDao.updateConsumable(consumable)

    override suspend fun upsertConsumable(consumable: Consumable) =consumableDao.upsertConsumable(consumable)

    override suspend fun insertToday(today: Today) = consumableDao.upsertToday(today)

    override suspend fun deleteToday(today: Today) = consumableDao.deleteToday(today)
    override suspend fun updateToday(today: Today) = consumableDao.updateToday(today)
    override suspend fun upsertToday(today: Today) = consumableDao.updateToday(today)
    override fun getTodayByIDStream(id: Long): Flow<Today?> = consumableDao.getTodayByID(id)
    override fun getAllTodayStream(): Flow<List<Today?>> = consumableDao.getAllToday()
    override fun getCurrentWeekToday(currentWeek: Int): Flow<List<Today>> = consumableDao.getCurrentWeek(currentWeek)
}