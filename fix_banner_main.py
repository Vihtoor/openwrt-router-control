with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """    val messages = mutableListOf<String>()
    val actions = mutableListOf<String>()
    
    if (!caps.hasBridgeUtil) {
        messages.add("Не найдена утилита bridge — определение устройств на LAN-портах недоступно.")
        actions.add("Установите пакет ip-bridge через opkg")
    }
    if (!caps.hasSwconfigUtil && caps.switchArchitecture != com.example.data.SwitchArchitecture.DSA) {
        messages.add("Не найдена утилита swconfig — недоступен линк-статус и трафик LAN-портов (если роутер поддерживает swconfig).")
        actions.add("Установите пакет swconfig через opkg")
    }
    
    if (caps.switchArchitecture == com.example.data.SwitchArchitecture.SWCONFIG) {
        if (!caps.hasBoardJsonWithSwitchSection) {
            messages.add("Невозможно сопоставить логические порты с физическими номерами LAN-портов.")
        }
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNSUPPORTED) {
        messages.add("Роутер/прошивка не поддерживает разделение LAN-портов (архитектура: ${caps.switchArchitecture}).")
    }
    
    val message = messages.joinToString("\\n\\n")
    val action = actions.joinToString("\\n")
    
    if (message.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(message, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium)
                if (action.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Установите утилиты для сбора статистики:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                    for (act in actions) {
                        val cmd = if (act.contains("ip-bridge")) "opkg update && opkg install ip-bridge" else if (act.contains("swconfig")) "opkg update && opkg install swconfig" else ""
                        if (cmd.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cmd,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                var btnFocused by remember { mutableStateOf(false) }
                                androidx.compose.material3.IconButton(
                                    onClick = {
                                        clipboard.setText(androidx.compose.ui.text.AnnotatedString(cmd))
                                    },
                                    modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        } else {
                            Text(act, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                androidx.compose.material3.Button(
                    onClick = {
                        prefs.edit().putBoolean("banner_dismissed_v2", true).apply()
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
    }"""

replacement = """    val lang = context.resources.configuration.locales[0].language
    val messages = mutableListOf<String>()
    val actions = mutableListOf<String>()
    
    if (!caps.hasBridgeUtil) {
        messages.add(com.example.TestTabLocalizations.getBannerNoBridge(lang))
        actions.add("ip-bridge")
    }
    if (!caps.hasSwconfigUtil && caps.switchArchitecture != com.example.data.SwitchArchitecture.DSA) {
        messages.add(com.example.TestTabLocalizations.getBannerNoSwconfig(lang))
        actions.add("swconfig")
    }
    
    if (caps.switchArchitecture == com.example.data.SwitchArchitecture.SWCONFIG) {
        if (!caps.hasBoardJsonWithSwitchSection) {
            messages.add(com.example.TestTabLocalizations.getBannerNoBoardJson(lang))
        }
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNSUPPORTED) {
        messages.add(com.example.TestTabLocalizations.getBannerUnsupported(lang, caps.switchArchitecture.toString()))
    }
    
    val message = messages.joinToString("\\n\\n")
    
    if (message.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(message, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium)
                if (actions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(com.example.TestTabLocalizations.getBannerInstallInstruction(lang), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                    for (act in actions) {
                        val cmd = if (act == "ip-bridge") "opkg update && opkg install ip-bridge" else if (act == "swconfig") "opkg update && opkg install swconfig" else ""
                        if (cmd.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cmd,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                var btnFocused by remember { mutableStateOf(false) }
                                androidx.compose.material3.IconButton(
                                    onClick = {
                                        clipboard.setText(androidx.compose.ui.text.AnnotatedString(cmd))
                                    },
                                    modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                androidx.compose.material3.Button(
                    onClick = {
                        prefs.edit().putBoolean("banner_dismissed_v2", true).apply()
                        isDismissed = true
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onErrorContainer, 
                        contentColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(com.example.TestTabLocalizations.getBannerGotIt(lang))
                }
            }
        }
    }"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
