package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RouterDao {
    @Query("SELECT * FROM router_config WHERE id = 1 LIMIT 1")
    fun getRouterConfigFlow(): Flow<RouterConfig?>

    @Query("SELECT * FROM router_config WHERE id = 1 LIMIT 1")
    suspend fun getRouterConfig(): RouterConfig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRouterConfig(config: RouterConfig)

    @Query("DELETE FROM router_config")
    suspend fun deleteRouterConfig()

    @Query("SELECT * FROM console_logs ORDER BY timestamp DESC LIMIT 200")
    fun getConsoleLogsFlow(): Flow<List<ConsoleLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsoleLog(log: ConsoleLog): Long

    @Query("DELETE FROM console_logs")
    suspend fun clearConsoleLogs()

    @Query("DELETE FROM console_logs WHERE command = :command")
    suspend fun deleteConsoleLogsByCommand(command: String)
}
