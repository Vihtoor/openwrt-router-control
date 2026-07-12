import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_dialog = """            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Тип: ${device.connectionType}", style = MaterialTheme.typography.bodyMedium)
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
                
                Spacer(modifier = Modifier.height(16.dp))"""

new_dialog = """            Column(modifier = Modifier.fillMaxWidth()) {
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
                
                Text(
                    text = "За сегодня: ↓ ${formatBytes(device.downloadBytes)} ↑ ${formatBytes(device.uploadBytes)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "С начала месяца: ↓ 0,0 КБ ↑ 0,0 КБ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))"""

if old_dialog in content:
    content = content.replace(old_dialog, new_dialog)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("Dialog Success")
else:
    print("Dialog Not found")
