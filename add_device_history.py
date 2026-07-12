with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

# Add deviceSpeedHistory to UiState
if "val deviceSpeedHistory: Map<String, List<DeviceSpeedInfo>>" not in content:
    content = content.replace("val deviceSpeeds: List<DeviceSpeedInfo> = emptyList(),", "val deviceSpeeds: List<DeviceSpeedInfo> = emptyList(),\n    val deviceSpeedHistory: Map<String, List<DeviceSpeedInfo>> = emptyMap(),")

# Update updateTelemetryOnly and addSpeedToHistory to handle deviceSpeedHistory
import re

calc_speeds_old = "val deviceSpeeds = calculateDeviceSpeeds(telemetry.devices, timeDiffSecs)"

if "deviceSpeedHistory" not in calc_speeds_old:
    content = content.replace(
        "val deviceSpeeds = calculateDeviceSpeeds(telemetry.devices, timeDiffSecs)",
        "val deviceSpeeds = calculateDeviceSpeeds(telemetry.devices, timeDiffSecs)\n                                val updatedDeviceHistory = currentState.deviceSpeedHistory.toMutableMap()\n                                for (device in deviceSpeeds) {\n                                    val history = updatedDeviceHistory[device.mac] ?: emptyList()\n                                    updatedDeviceHistory[device.mac] = (history + device).takeLast(30)\n                                }"
    )

    content = content.replace(
        "addSpeedToHistory(rxSpeedMbps, txSpeedMbps, telemetry.cpuUsage, telemetry.memoryUsage, telemetry.uptime, deviceSpeeds)",
        "addSpeedToHistory(rxSpeedMbps, txSpeedMbps, telemetry.cpuUsage, telemetry.memoryUsage, telemetry.uptime, deviceSpeeds, updatedDeviceHistory)"
    )
    
    content = content.replace(
        "updateTelemetryOnly(telemetry.cpuUsage, telemetry.memoryUsage, telemetry.uptime, calculateDeviceSpeeds(telemetry.devices, 2.0))",
        "val speeds = calculateDeviceSpeeds(telemetry.devices, 2.0)\n                        val updatedDeviceHistory = currentState.deviceSpeedHistory.toMutableMap()\n                        for (device in speeds) {\n                            val history = updatedDeviceHistory[device.mac] ?: emptyList()\n                            updatedDeviceHistory[device.mac] = (history + device).takeLast(30)\n                        }\n                        updateTelemetryOnly(telemetry.cpuUsage, telemetry.memoryUsage, telemetry.uptime, speeds, updatedDeviceHistory)"
    )

    content = content.replace(
        "private fun addSpeedToHistory(download: Float, upload: Float, cpu: String, mem: String, uptime: String, devices: List<DeviceSpeedInfo>)",
        "private fun addSpeedToHistory(download: Float, upload: Float, cpu: String, mem: String, uptime: String, devices: List<DeviceSpeedInfo>, deviceHistory: Map<String, List<DeviceSpeedInfo>>)"
    )

    content = content.replace(
        "deviceSpeeds = devices\n            )",
        "deviceSpeeds = devices,\n                deviceSpeedHistory = deviceHistory\n            )"
    )

    content = content.replace(
        "private fun updateTelemetryOnly(cpu: String, mem: String, uptime: String, devices: List<DeviceSpeedInfo>)",
        "private fun updateTelemetryOnly(cpu: String, mem: String, uptime: String, devices: List<DeviceSpeedInfo>, deviceHistory: Map<String, List<DeviceSpeedInfo>>)"
    )

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)

