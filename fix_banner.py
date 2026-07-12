import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    main = f.read()

# 1. Remove from DashboardTab
old_dashboard_item = """        item {
            CapabilitiesBanner(state.config)
        }"""
main = main.replace(old_dashboard_item, "")

# 2. Add config to DeviceListDialog signature
main = main.replace("fun DeviceListDialog(\n    title: String,\n    devices: List<com.example.ui.DeviceSpeedInfo>,", 
                    "fun DeviceListDialog(\n    title: String,\n    config: com.example.data.RouterConfig?,\n    devices: List<com.example.ui.DeviceSpeedInfo>,")

main = main.replace("DeviceListDialog(\n                title = \"Устройства (Загрузка ↓)\",\n                devices = state.deviceSpeeds,",
                    "DeviceListDialog(\n                title = \"Устройства (Загрузка ↓)\",\n                config = state.config,\n                devices = state.deviceSpeeds,")

main = main.replace("DeviceListDialog(\n                title = \"Устройства (Выгрузка ↑)\",\n                devices = state.deviceSpeeds,",
                    "DeviceListDialog(\n                title = \"Устройства (Выгрузка ↑)\",\n                config = state.config,\n                devices = state.deviceSpeeds,")

# 3. Add to tablet layout
tablet_target = """                    }
                    if (sortedDevices.isEmpty()) {"""
tablet_replace = """                    }
                    CapabilitiesBanner(config)
                    if (sortedDevices.isEmpty()) {"""
main = main.replace(tablet_target, tablet_replace)

# 4. Add to phone layout
phone_target = """                            )
                        }
                        // Content
                        Box("""
phone_replace = """                            )
                        }
                        CapabilitiesBanner(config)
                        // Content
                        Box("""
main = main.replace(phone_target, phone_replace)

# 5. Rewrite CapabilitiesBanner
old_banner = """@Composable
fun CapabilitiesBanner(config: com.example.data.RouterConfig?) {
    if (config == null) return
    val caps = config.capabilities
    if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNKNOWN) return
    
    var message = ""
    var action = ""
    
    if (!caps.hasBridgeUtil) {
        message = "Не найдена утилита bridge — определение устройств на LAN-портах недоступно."
        action = "Установите пакет ip-bridge через opkg"
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.SWCONFIG) {
        if (!caps.hasSwconfigUtil) {
            message = "Не найдена утилита swconfig — недоступен линк-статус и трафик LAN-портов на этом типе роутера."
            action = "Установите пакет swconfig через opkg"
        } else if (!caps.hasBoardJsonWithSwitchSection) {
            message = "Невозможно сопоставить логические порты с физическими номерами LAN-портов."
        }
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNSUPPORTED) {
        message = "Роутер/прошивка не поддерживает разделение LAN-портов (например, простой роутер без управляемого свича, где все LAN-порты объединены в один eth0)."
    }
    
    if (message.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(message, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium)
                if (action.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(action, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}"""

new_banner = """@Composable
fun CapabilitiesBanner(config: com.example.data.RouterConfig?) {
    if (config == null) return
    val caps = config.capabilities
    if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNKNOWN) return
    
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE) }
    var isDismissed by remember { mutableStateOf(prefs.getBoolean("banner_dismissed", false)) }
    
    if (isDismissed) return

    var message = ""
    var action = ""
    
    if (!caps.hasBridgeUtil) {
        message = "Не найдена утилита bridge — определение устройств на LAN-портах недоступно."
        action = "Установите пакет ip-bridge через opkg"
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.SWCONFIG) {
        if (!caps.hasSwconfigUtil) {
            message = "Не найдена утилита swconfig — недоступен линк-статус и трафик LAN-портов на этом типе роутера."
            action = "Установите пакет swconfig через opkg"
        } else if (!caps.hasBoardJsonWithSwitchSection) {
            message = "Невозможно сопоставить логические порты с физическими номерами LAN-портов."
        }
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNSUPPORTED) {
        message = "Роутер/прошивка не поддерживает разделение LAN-портов (например, простой роутер без управляемого свича, где все LAN-порты объединены в один eth0)."
    }
    
    if (message.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(message, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium)
                if (action.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(action, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                androidx.compose.material3.Button(
                    onClick = {
                        prefs.edit().putBoolean("banner_dismissed", true).apply()
                        isDismissed = true
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onErrorContainer, 
                        contentColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text("Понятно")
                }
            }
        }
    }
}"""

main = main.replace(old_banner, new_banner)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(main)

