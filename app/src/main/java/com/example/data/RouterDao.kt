package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RouterDao {
    @Query("SELECT * FROM router_config WHERE isActive = 1 LIMIT 1")
    fun getActiveRouterConfigFlow(): Flow<RouterConfig?>

    @Query("SELECT * FROM router_config ORDER BY id ASC")
    fun getAllRouterConfigsFlow(): Flow<List<RouterConfig>>

    @Query("SELECT * FROM router_config WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveRouterConfig(): RouterConfig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRouterConfig(config: RouterConfig): Long

    @Query("UPDATE router_config SET isActive = 0")
    suspend fun deactivateAll()

    @Query("DELETE FROM router_config WHERE id = :id")
    suspend fun deleteRouterConfig(id: Int)

    @Query("SELECT * FROM console_logs ORDER BY timestamp DESC LIMIT 200")
    fun getConsoleLogsFlow(): Flow<List<ConsoleLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsoleLog(log: ConsoleLog): Long

    @Query("DELETE FROM console_logs")
    suspend fun clearConsoleLogs()

    @Query("DELETE FROM console_logs WHERE command = :command")
    suspend fun deleteConsoleLogsByCommand(command: String)
}
