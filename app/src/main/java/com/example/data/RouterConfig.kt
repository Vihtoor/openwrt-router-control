package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "router_config")
data class RouterConfig(
    @PrimaryKey val id: Int = 1,
    val ipAddress: String,
    val port: Int = 22,
    val username: String,
    val sshKeyOrPassword: String,
    val ledBehavior: String = "always_on",
    val wgInterface: String = "wg0",
    val openVpnService: String = "openvpn"
)
