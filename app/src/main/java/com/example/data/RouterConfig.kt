package com.example.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SwitchArchitecture {
    UNKNOWN, DSA, SWCONFIG, UNSUPPORTED
}

data class RouterCapabilities(
    val switchArchitecture: SwitchArchitecture = SwitchArchitecture.UNKNOWN,
    val hasBridgeUtil: Boolean = false,
    val hasSwconfigUtil: Boolean = false,
    val hasBoardJsonWithSwitchSection: Boolean = false,
    val hasMibCounters: Boolean = false,
    val hasWiredFdbSupport: Boolean = true,
    val temperatureSource: String = "UNKNOWN",
    val temperaturePaths: String = "",
    val lastCheckedTimestamp: Long = 0L
)

@Entity(tableName = "router_config")
data class RouterConfig(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val profileName: String = "My Router",
    val ipAddress: String,
    val port: Int = 22,
    val username: String,
    val sshKeyOrPassword: String,
    val ledBehavior: String = "always_on",
    val wgInterface: String = "wg0",
    val openVpnService: String = "openvpn",
    val isActive: Boolean = true,
    @Embedded val capabilities: RouterCapabilities = RouterCapabilities()
)
