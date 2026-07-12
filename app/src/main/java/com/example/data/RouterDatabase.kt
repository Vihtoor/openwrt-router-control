package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RouterConfig::class, ConsoleLog::class], version = 9, exportSchema = false)
abstract class RouterDatabase : RoomDatabase() {
    abstract fun routerDao(): RouterDao

    companion object {
        @Volatile
        private var INSTANCE: RouterDatabase? = null

        fun getDatabase(context: Context): RouterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RouterDatabase::class.java,
                    "router_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
