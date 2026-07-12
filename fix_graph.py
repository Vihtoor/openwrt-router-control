import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Fix 1: Remove fillMaxHeight before verticalScroll
old_scroll = """Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState())) {"""
new_scroll = """Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {"""
if old_scroll in content:
    content = content.replace(old_scroll, new_scroll)
    print("Scroll fixed")
else:
    print("Scroll NOT FOUND")

# Fix 2: Make the graph background darker
old_graph_tablet = """                                SpeedChart(
                                    history = convertedHistory,
                                    modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.05f))
                                )"""
new_graph_tablet = """                                SpeedChart(
                                    history = convertedHistory,
                                    modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.15f))
                                )"""
if old_graph_tablet in content:
    content = content.replace(old_graph_tablet, new_graph_tablet)
    print("Graph tablet fixed")
else:
    print("Graph tablet NOT FOUND")

old_graph_phone = """                    SpeedChart(
                        history = convertedHistory,
                        modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.05f))
                    )"""
new_graph_phone = """                    SpeedChart(
                        history = convertedHistory,
                        modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.15f))
                    )"""
if old_graph_phone in content:
    content = content.replace(old_graph_phone, new_graph_phone)
    print("Graph phone fixed")
else:
    print("Graph phone NOT FOUND")

# Fix 3: Make traffic data bold in DeviceListDialog
old_row_tablet = """                            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("За сегодня: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("↓ ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                                Text("${formatBytes(rightPaneDevice.downloadBytes)} ", style = MaterialTheme.typography.bodySmall, color = dlColor)
                                Text("↑ ", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                                Text("${formatBytes(rightPaneDevice.uploadBytes)}", style = MaterialTheme.typography.bodySmall, color = ulColor)
                            }"""
new_row_tablet = """                            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("За сегодня: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("↓ ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                                Text("${formatBytes(rightPaneDevice.downloadBytes)} ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                                Text("↑ ", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                                Text("${formatBytes(rightPaneDevice.uploadBytes)}", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                            }"""
if old_row_tablet in content:
    content = content.replace(old_row_tablet, new_row_tablet)
    print("Row tablet fixed")
else:
    print("Row tablet NOT FOUND")

# Fix 4: Make traffic data bold in DeviceSpeedDialog
old_row_phone = """                androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("За сегодня: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("↓ ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(device.downloadBytes)} ", style = MaterialTheme.typography.bodySmall, color = dlColor)
                    Text("↑ ", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(device.uploadBytes)}", style = MaterialTheme.typography.bodySmall, color = ulColor)
                }"""
new_row_phone = """                androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("За сегодня: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("↓ ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(device.downloadBytes)} ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                    Text("↑ ", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(device.uploadBytes)}", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                }"""
if old_row_phone in content:
    content = content.replace(old_row_phone, new_row_phone)
    print("Row phone fixed")
else:
    print("Row phone NOT FOUND")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
