import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# We'll replace both functions at once

pattern = re.compile(r'@Composable\s*fun DeviceListDialog\(.*?fun DeviceSpeedDialog\(.*?}\n\s*}\n}', re.DOTALL)

replacement = """@Composable
fun DeviceListDialog(
    title: String,
    devices: List<com.example.ui.DeviceSpeedInfo>,
    deviceHistory: Map<String, List<com.example.ui.DeviceSpeedInfo>>,
    isDownload: Boolean,
    onDismiss: () -> Unit
) {
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    var selectedDevice by remember { mutableStateOf<com.example.ui.DeviceSpeedInfo?>(null) }
    var focusedDeviceMac by remember { mutableStateOf<String?>(null) }
    
    val listBg = MaterialTheme.colorScheme.surface
    val headerBg = androidx.compose.ui.graphics.lerp(listBg, Color.White, 0.08f)
    val sortedDevices = devices.sortedByDescending { it.downloadBytes }

    if (isTablet) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = onDismiss,
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Row(modifier = Modifier.fillMaxSize().background(listBg)) {
                Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerBg)
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Выберите устройство, чтобы открыть график",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (sortedDevices.isEmpty()) {
                        Text(
                            text = "Нет подключенных устройств или данные недоступны.",
                            modifier = Modifier.padding(24.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
                            itemsIndexed(sortedDevices) { index, device ->
                                var isFocused by remember { mutableStateOf(false) }
                                val isSelected = focusedDeviceMac == device.mac || (focusedDeviceMac == null && index == 0)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { focusedDeviceMac = device.mac }
                                        .onFocusChanged { 
                                            isFocused = it.isFocused
                                            if (it.isFocused) {
                                                focusedDeviceMac = device.mac
                                            }
                                        }
                                        .focusable()
                                        .background(if (isFocused || isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                        .padding(vertical = 16.dp, horizontal = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = device.hostname,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected || isFocused) Color.Red else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(device.connectionType, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    val speed = if (isDownload) device.downloadSpeedMbps else device.uploadSpeedMbps
                                    val speedColor = if (isDownload) {
                                        if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                                    } else {
                                        Color(0xFFFFB300)
                                    }
                                    val totalBytes = if (isDownload) device.downloadBytes else device.uploadBytes
                                    val totalBytesStr = if (totalBytes < 1024 * 1024) {
                                        String.format("сегодня %.1f КБ", totalBytes / 1024f)
                                    } else if (totalBytes < 1024 * 1024 * 1024) {
                                        String.format("сегодня %.1f МБ", totalBytes / (1024f * 1024f))
                                    } else {
                                        String.format("сегодня %.1f ГБ", totalBytes / (1024f * 1024f * 1024f))
                                    }
                                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                                        Text(
                                            text = String.format("%.2f Мбит/с", speed),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = speedColor
                                        )
                                        Text(
                                            text = totalBytesStr,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                if (index < sortedDevices.size - 1) {
                                    androidx.compose.material3.HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 4.dp),
                                        color = androidx.compose.ui.graphics.lerp(listBg, Color.White, 0.12f),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                }
                
                val currentMac = focusedDeviceMac ?: sortedDevices.firstOrNull()?.mac
                val rightPaneDevice = sortedDevices.find { it.mac == currentMac }
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(headerBg)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (rightPaneDevice != null) {
                        val history = deviceHistory[rightPaneDevice.mac] ?: listOf(rightPaneDevice)
                        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState())) {
                            Text(rightPaneDevice.hostname, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(rightPaneDevice.connectionType, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                            Text(
                                text = String.format("Загрузка: %.2f Мбит/с", rightPaneDevice.downloadSpeedMbps),
                                color = dlColor,
                                fontWeight = FontWeight.Bold
                            )
                            
                            val ulColor = Color(0xFFFFB300)
                            Text(
                                text = String.format("Выгрузка: %.2f Мбит/с", rightPaneDevice.uploadSpeedMbps),
                                color = ulColor,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))

                            val formatBytes = { bytes: Long ->
                                if (bytes < 1024 * 1024) {
                                    String.format("%.1f КБ", bytes / 1024f)
                                } else if (bytes < 1024 * 1024 * 1024) {
                                    String.format("%.1f МБ", bytes / (1024f * 1024f))
                                } else {
                                    String.format("%.1f ГБ", bytes / (1024f * 1024f * 1024f))
                                }
                            }
                            
                            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("За сегодня: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("↓ ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                                Text("${formatBytes(rightPaneDevice.downloadBytes)} ", style = MaterialTheme.typography.bodySmall, color = dlColor)
                                Text("↑ ", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                                Text("${formatBytes(rightPaneDevice.uploadBytes)}", style = MaterialTheme.typography.bodySmall, color = ulColor)
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            val convertedHistory = history.map { 
                                com.example.ui.SpeedSnapshot(
                                    downloadSpeedMbps = it.downloadSpeedMbps,
                                    uploadSpeedMbps = it.uploadSpeedMbps,
                                    cpuUsagePercent = 0f
                                )
                            }
                            
                            Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                                SpeedChart(
                                    history = convertedHistory,
                                    modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.05f))
                                )
                            }
                        }
                    } else {
                        Text("Нет выбранного устройства", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    } else {
        if (selectedDevice != null) {
            val history = deviceHistory[selectedDevice!!.mac] ?: listOf(selectedDevice!!)
            DeviceSpeedDialog(
                device = selectedDevice!!,
                history = history,
                onDismiss = { selectedDevice = null }
            )
        } else {
            androidx.compose.ui.window.Dialog(
                onDismissRequest = onDismiss,
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.systemBars)
                        .padding(top = 0.dp, bottom = 120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.width(360.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = listBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                    Column {
                        // Header
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(headerBg)
                                .padding(horizontal = 12.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Нажмите на устройство, чтобы открыть график",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        // Content
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = false)
                                .padding(horizontal = 12.dp)
                        ) {
                            if (sortedDevices.isEmpty()) {
                                Text(
                                    text = "Нет подключенных устройств или данные недоступны.",
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                androidx.compose.foundation.lazy.LazyColumn {
                                    itemsIndexed(sortedDevices) { index, device ->
                                        var isFocused by remember { mutableStateOf(false) }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { selectedDevice = device }
                                                .onFocusChanged { isFocused = it.isFocused }
                                                .focusable()
                                                .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                                .padding(vertical = 12.dp, horizontal = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(device.hostname, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                                Text(device.connectionType, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            val speed = if (isDownload) device.downloadSpeedMbps else device.uploadSpeedMbps
                                            val speedColor = if (isDownload) {
                                                if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                                            } else {
                                                Color(0xFFFFB300)
                                            }
                                            val totalBytes = if (isDownload) device.downloadBytes else device.uploadBytes
                                            val totalBytesStr = if (totalBytes < 1024 * 1024) {
                                                String.format("сегодня %.1f КБ", totalBytes / 1024f)
                                            } else if (totalBytes < 1024 * 1024 * 1024) {
                                                String.format("сегодня %.1f МБ", totalBytes / (1024f * 1024f))
                                            } else {
                                                String.format("сегодня %.1f ГБ", totalBytes / (1024f * 1024f * 1024f))
                                            }
                                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                                                Text(
                                                    text = String.format("%.2f Мбит/с", speed),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = speedColor
                                                )
                                                Text(
                                                    text = totalBytesStr,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                        if (index < sortedDevices.size - 1) {
                                            androidx.compose.material3.HorizontalDivider(
                                                modifier = Modifier.padding(horizontal = 4.dp),
                                                color = androidx.compose.ui.graphics.lerp(listBg, Color.White, 0.12f),
                                                thickness = 1.dp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        // Bottom Panel
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(headerBg)
                                .height(52.dp)
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            var isFocused by remember { mutableStateOf(false) }
                            androidx.compose.material3.TextButton(
                                onClick = onDismiss,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                                    .onFocusChanged { isFocused = it.isFocused }
                                    .focusable()
                                    .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(50))
                            ) {
                                Text("Закрыть")
                            }
                        }
                    }
                }
                }
            }
        }
    }
}

@Composable
fun DeviceSpeedDialog(
    device: com.example.ui.DeviceSpeedInfo,
    history: List<com.example.ui.DeviceSpeedInfo>,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(device.hostname) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(device.connectionType, style = MaterialTheme.typography.bodyMedium)
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
                
                Spacer(modifier = Modifier.height(16.dp))

                val formatBytes = { bytes: Long ->
                    if (bytes < 1024 * 1024) {
                        String.format("%.1f КБ", bytes / 1024f)
                    } else if (bytes < 1024 * 1024 * 1024) {
                        String.format("%.1f МБ", bytes / (1024f * 1024f))
                    } else {
                        String.format("%.1f ГБ", bytes / (1024f * 1024f * 1024f))
                    }
                }
                
                androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("За сегодня: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("↓ ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(device.downloadBytes)} ", style = MaterialTheme.typography.bodySmall, color = dlColor)
                    Text("↑ ", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(device.uploadBytes)}", style = MaterialTheme.typography.bodySmall, color = ulColor)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val convertedHistory = history.map { 
                    com.example.ui.SpeedSnapshot(
                        downloadSpeedMbps = it.downloadSpeedMbps,
                        uploadSpeedMbps = it.uploadSpeedMbps,
                        cpuUsagePercent = 0f
                    )
                }
                
                Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    SpeedChart(
                        history = convertedHistory,
                        modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.05f))
                    )
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
}"""

match = pattern.search(content)
if match:
    new_content = content[:match.start()] + replacement + content[match.end():]
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(new_content)
    print("Successfully replaced.")
else:
    print("Match not found.")
    
    # Try finding the end of DeviceSpeedDialog dynamically
    import re
    device_list_start = content.find("@Composable\nfun DeviceListDialog(")
    if device_list_start != -1:
        # find end of DeviceSpeedDialog
        device_speed_start = content.find("@Composable\nfun DeviceSpeedDialog(", device_list_start)
        if device_speed_start != -1:
            # count brackets to find end
            brace_count = 0
            idx = device_speed_start
            found_first = False
            for i in range(device_speed_start, len(content)):
                if content[i] == '{':
                    brace_count += 1
                    found_first = True
                elif content[i] == '}':
                    brace_count -= 1
                    if found_first and brace_count == 0:
                        idx = i + 1
                        break
            if found_first and brace_count == 0:
                new_content = content[:device_list_start] + replacement + content[idx:]
                with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
                    f.write(new_content)
                print("Fallback successful.")
            else:
                print("Could not find end of DeviceSpeedDialog")
