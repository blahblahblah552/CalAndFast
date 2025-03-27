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
    //Consumable

    @Upsert
    suspend fun upsertConsumable(consumable: Consumable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateConsumable(consumable: Consumable)

    @Delete
    suspend fun deleteConsumable(consumable: Consumable)

    @Query("SELECT * from consumable WHERE consumableId = :id")
    fun getItem(id: Int): Flow<Consumable>

    @Query("SELECT * from consumable ORDER BY name ASC")
    fun getAllItems(): Flow<List<Consumable>>

    @Query("SELECT * from consumable ORDER BY lastUsed DESC")
    fun getAllItemsByLastUsed(): Flow<List<Consumable>>

    @Query("SELECT * from consumable ORDER BY calories ASC")
    fun getAllItemsByCalories(): Flow<List<Consumable>>

    @Query("SELECT * from consumable WHERE lastUsed >= :currentWeek ORDER BY lastUsed ASC")
    fun getCurrentWeekConsumable(currentWeek: Long): Flow<List<Consumable>>

    //TODAY

    @Upsert
    suspend fun upsertToday(today: Today)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateToday(today: Today)

    @Delete
    suspend fun deleteToday(today: Today)

    @Query("SELECT * FROM today WHERE date = :id")
    fun getTodayByID(id: Long): Flow<Today>

    @Query("SELECT * FROM today WHERE currentWeek = :currentWeek ORDER BY date DESC")
    fun  getCurrentWeek(currentWeek: Int): Flow<List<Today>>

    @Query("SELECT * from today ORDER BY date DESC")
    fun getAllToday(): Flow<List<Today>>
}