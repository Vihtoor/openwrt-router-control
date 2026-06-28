package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "console_logs")
data class ConsoleLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val command: String,
    val output: String,
    val timestamp: Long = System.currentTimeMillis()
)
