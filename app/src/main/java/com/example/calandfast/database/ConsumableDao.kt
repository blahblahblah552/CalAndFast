package com.example.calandfast.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumableDao {
    @Upsert
    suspend fun upsertConsumable(consumable: Consumable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateConsumable(consumable: Consumable)

    @Delete
    suspend fun deleteConsumable(consumable: Consumable)

    @Query("SELECT * from consumable WHERE id = :id")
    fun getItem(id: Int): Flow<Consumable>

    @Query("SELECT * from consumable ORDER BY name ASC")
    fun getAllItems(): Flow<List<Consumable>>

    @Query("SELECT * from consumable ORDER BY lastUsed ASC")
    fun getAllItemsByLastUsed(): Flow<List<Consumable>>

    @Query("SELECT * from consumable ORDER BY calories ASC")
    fun getAllItemsByCalories(): Flow<List<Consumable>>
}