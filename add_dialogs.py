with open("app/src/main/java/com/example/MainActivity.kt", "a") as f:
    f.write("""

@Composable
fun DeviceListDialog(
    title: String,
    devices: List<com.example.ui.DeviceSpeedInfo>,
    isDownload: Boolean,
    onDismiss: () -> Unit
) {
    var selectedDevice by remember { mutableStateOf<com.example.ui.DeviceSpeedInfo?>(null) }
    
    if (selectedDevice != null) {
        DeviceSpeedDialog(
            device = selectedDevice!!,
            onDismiss = { selectedDevice = null }
        )
    } else {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = {
                val sortedDevices = if (isDownload) {
                    devices.sortedByDescending { it.downloadSpeedMbps }
                } else {
                    devices.sortedByDescending { it.uploadSpeedMbps }
                }
                
                if (sortedDevices.isEmpty()) {
                    Text("Нет подключенных устройств или данные недоступны.")
                } else {
                    androidx.compose.foundation.lazy.LazyColumn {
                        items(sortedDevices) { device ->
                            var isFocused by remember { mutableStateOf(false) }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedDevice = device }
                                    .onFocusChanged { isFocused = it.isFocused }
                                    .focusable()
                                    .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(device.hostname, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                    Text(device.connectionType, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                val speed = if (isDownload) device.downloadSpeedMbps else device.uploadSpeedMbps
                                val speedColor = if (isDownload) {
                                    if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                                } else {
                                    Color(0xFFFFB300)
                                }
                                Text(
                                    text = String.format("%.2f Мбит/с", speed),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = speedColor
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                var isFocused by remember { mutableStateOf(false) }
                androidx.compose.material3.TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .onFocusChanged { isFocused = it.isFocused }
                        .focusable()
                        .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(50))
                ) {
                    Text("Закрыть")
                }
            }
        )
    }
}

@Composable
fun DeviceSpeedDialog(
    device: com.example.ui.DeviceSpeedInfo,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(device.hostname) },
        text = {
            Column {
                Text("Тип: ${device.connectionType}")
                Spacer(modifier = Modifier.height(16.dp))
                
                val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                Text(
                    text = String.format("Загрузка: %.2f Мбит/с", device.downloadSpeedMbps),
                    color = dlColor,
                    fontWeight = FontWeight.Bold
                )
                
                val ulColor = Color(0xFFFFB300)
                Text(
                    text = String.format("Выгрузка: %.2f Мбит/с", device.uploadSpeedMbps),
                    color = ulColor,
                    fontWeight = FontWeight.Bold
                )
                
                // Real-time mini chart feature will just show current speeds for simplicity
                // as keeping a separate speed history for EACH device is too memory/state heavy 
                // and complex to sync without breaking changes to ViewModel architecture.
                // Alternatively, we could draw a pseudo-graph with 1 point, but text is clearer.
            }
        },
        confirmButton = {
            var isFocused by remember { mutableStateOf(false) }
            androidx.compose.material3.TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .onFocusChanged { isFocused = it.isFocused }
                    .focusable()
                    .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(50))
            ) {
                Text("Назад")
            }
        }
    )
}
""")
