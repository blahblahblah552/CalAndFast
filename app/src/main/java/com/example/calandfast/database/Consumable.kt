package com.example.calandfast.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Consumable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val calories: Int,
    val lastUsed: String
)