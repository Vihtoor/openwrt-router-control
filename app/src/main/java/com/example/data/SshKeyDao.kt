package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SshKeyDao {
    @Query("SELECT * FROM ssh_keys ORDER BY createdAt DESC")
    fun getAllKeysFlow(): Flow<List<SshKeyEntity>>

    @Query("SELECT * FROM ssh_keys ORDER BY createdAt DESC")
    suspend fun getAllKeys(): List<SshKeyEntity>

    @Query("SELECT * FROM ssh_keys WHERE id = :keyId LIMIT 1")
    suspend fun getKeyById(keyId: Long): SshKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(key: SshKeyEntity): Long

    @Delete
    suspend fun deleteKey(key: SshKeyEntity)
}
