import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_ui = """                Text(
                    text = "За сегодня: ↓ ${formatBytes(device.downloadBytes)} ↑ ${formatBytes(device.uploadBytes)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "С начала месяца: ↓ ${formatBytes(0L)} ↑ ${formatBytes(0L)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )"""

new_ui = """                androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("За сегодня: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("↓ ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(device.downloadBytes)} ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("↑ ", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(device.uploadBytes)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("С начала месяца: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("↓ ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(device.downloadMonthBytes)} ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("↑ ", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(device.uploadMonthBytes)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }"""

if old_ui in content:
    content = content.replace(old_ui, new_ui)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("UI updated")
else:
    print("UI not found")
