package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RouterConfig::class, ConsoleLog::class, SshKeyEntity::class], version = 10, exportSchema = false)
abstract class RouterDatabase : RoomDatabase() {
    abstract fun routerDao(): RouterDao
    abstract fun sshKeyDao(): SshKeyDao

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
