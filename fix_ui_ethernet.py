import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    main = f.read()

# For DeviceSpeedDialog:
old_dialog = """                            val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
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
                            )"""
                            
new_dialog = """                            val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                            val ulColor = Color(0xFFFFB300)
                            if (rightPaneDevice.wifiRxBitrate != null && rightPaneDevice.wifiTxBitrate != null) {
                                Text("Скорость линка: ↓ ${rightPaneDevice.wifiRxBitrate} / ↑ ${rightPaneDevice.wifiTxBitrate}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                            }
                            if (rightPaneDevice.downloadBytes == -1L && rightPaneDevice.uploadBytes == -1L) {
                                Text("Трафик недоступен на этом роутере", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                            } else {
                                Text(
                                    text = String.format("Загрузка: %.2f Мбит/с", rightPaneDevice.downloadSpeedMbps),
                                    color = dlColor,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = String.format("Выгрузка: %.2f Мбит/с", rightPaneDevice.uploadSpeedMbps),
                                    color = ulColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }"""

main = main.replace(old_dialog, new_dialog)

old_dialog_modal = """                val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
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
                )"""

new_dialog_modal = """                val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                val ulColor = Color(0xFFFFB300)
                if (device.wifiRxBitrate != null && device.wifiTxBitrate != null) {
                    Text("Скорость линка: ↓ ${device.wifiRxBitrate} / ↑ ${device.wifiTxBitrate}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                if (device.downloadBytes == -1L && device.uploadBytes == -1L) {
                    Text("Трафик недоступен на этом роутере", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                } else {
                    Text(
                        text = String.format("Загрузка: %.2f Мбит/с", device.downloadSpeedMbps),
                        color = dlColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = String.format("Выгрузка: %.2f Мбит/с", device.uploadSpeedMbps),
                        color = ulColor,
                        fontWeight = FontWeight.Bold
                    )
                }"""
                
main = main.replace(old_dialog_modal, new_dialog_modal)

old_list_item = """                                            val speed = if (isDownload) device.downloadSpeedMbps else device.uploadSpeedMbps
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
                                            }"""

new_list_item = """                                            val speedColor = if (isDownload) {
                                                if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                                            } else {
                                                Color(0xFFFFB300)
                                            }
                                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                                                if (device.downloadBytes == -1L && device.uploadBytes == -1L) {
                                                    Text("—", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = speedColor)
                                                } else {
                                                    val speed = if (isDownload) device.downloadSpeedMbps else device.uploadSpeedMbps
                                                    val totalBytes = if (isDownload) device.downloadBytes else device.uploadBytes
                                                    val totalBytesStr = if (totalBytes < 1024 * 1024) {
                                                        String.format("сегодня %.1f КБ", totalBytes / 1024f)
                                                    } else if (totalBytes < 1024 * 1024 * 1024) {
                                                        String.format("сегодня %.1f МБ", totalBytes / (1024f * 1024f))
                                                    } else {
                                                        String.format("сегодня %.1f ГБ", totalBytes / (1024f * 1024f * 1024f))
                                                    }
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
                                            }"""

main = main.replace(old_list_item, new_list_item)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(main)
