package com.example.calandfast.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Database class with a singleton Instance object.
 */
@Database(
    entities = [Consumable::class,Today::class],
    version = 2,
    exportSchema = false)
@TypeConverters(Converters::class )
abstract class ConsumableDatabase : RoomDatabase() {

    abstract fun consumableDao(): ConsumableDao

    companion object {
        @Volatile
        private var Instance: ConsumableDatabase? = null

        fun getDatabase(context: Context): ConsumableDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ConsumableDatabase::class.java, "consumable_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}