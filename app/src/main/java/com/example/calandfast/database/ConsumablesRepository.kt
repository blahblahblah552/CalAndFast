package com.example.calandfast.database

import kotlinx.coroutines.flow.Flow

interface ConsumablesRepository {
    //Consumable
    fun getAllConsumablesStream(): Flow<List<Consumable>>

    fun getConsumableStream(id: Int): Flow<Consumable?>

    fun getCurrentWeekConsumable(currentWeek: Long): Flow<List<Consumable>>

    suspend fun insertConsumable(consumable: Consumable)

    suspend fun deleteConsumable(consumable: Consumable)

    suspend fun updateConsumable(consumable: Consumable)
    
    suspend fun upsertConsumable(consumable: Consumable)


    //Today
    suspend fun insertToday(today: Today)

    suspend fun deleteToday(today: Today)

    suspend fun updateToday(today: Today)

    suspend fun upsertToday(today: Today)

    fun getTodayByIDStream(id: Long): Flow<Today?>

    fun getAllTodayStream(): Flow<List<Today?>>

    fun getCurrentWeekToday(currentWeek: Int): Flow<List<Today>>
}