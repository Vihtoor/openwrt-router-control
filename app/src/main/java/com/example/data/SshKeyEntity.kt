package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ssh_keys")
data class SshKeyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val alias: String,
    val algorithm: String, // e.g. "ED25519" or "RSA"
    val fingerprintSha256: String,
    val createdAt: Long = System.currentTimeMillis()
)
