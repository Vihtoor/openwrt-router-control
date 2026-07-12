with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

import re

# We need to replace the content of itemsIndexed(devicesInGroup) 
old_bad_row = """                                                Row(
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
                                                        Text("IP: ${device.ip} • MAC: ${device.mac}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                    }
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                                                                        }
                                                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                                                        if (device.downloadBytes == -1L && device.uploadBytes == -1L) {
                                                            Text("—", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = speedColor)
                                                        } else {
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
                                                    }
                                                }
                                                if (index < devicesInGroup.size - 1) {
                                                    androidx.compose.material3.Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 1.dp)
                                                }
"""

new_good_row = """                                                Row(
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
                                                        Text("IP: ${device.ip}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                        Text("MAC: ${device.mac}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                    }
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                                                        val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                                                        val ulColor = Color(0xFFFFB300)
                                                        if (device.downloadBytes == -1L && device.uploadBytes == -1L) {
                                                            Text("—", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                        } else {
                                                            Text("↓ ${String.format(java.util.Locale.US, "%.2f", device.downloadSpeedMbps)} Мбит/с", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = dlColor)
                                                            Text("↑ ${String.format(java.util.Locale.US, "%.2f", device.uploadSpeedMbps)} Мбит/с", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = ulColor)
                                                            
                                                            val totalBytes = device.downloadBytes + device.uploadBytes
                                                            val trafficStr = if (totalBytes < 1024 * 1024) {
                                                                String.format(java.util.Locale.US, "%.1f КБ", totalBytes / 1024f)
                                                            } else if (totalBytes < 1024 * 1024 * 1024) {
                                                                String.format(java.util.Locale.US, "%.1f МБ", totalBytes / (1024f * 1024f))
                                                            } else {
                                                                String.format(java.util.Locale.US, "%.1f ГБ", totalBytes / (1024f * 1024f * 1024f))
                                                            }
                                                            Text(androidx.compose.ui.res.stringResource(R.string.traffic_label) + " " + trafficStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                        }
                                                    }
                                                }
                                                if (index < devicesInGroup.size - 1) {
                                                    androidx.compose.material3.Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 1.dp)
                                                }
"""

if old_bad_row in content:
    content = content.replace(old_bad_row, new_good_row)
else:
    # Use regex to find and replace everything between `Row(` and `if (index < devicesInGroup.size - 1)`
    content = re.sub(r'Row\(\s*modifier = Modifier\s*\.fillMaxWidth\(\)\s*\.clip.*?if \(index < devicesInGroup\.size - 1\) \{', new_good_row.split('if (index < devicesInGroup.size - 1) {')[0] + 'if (index < devicesInGroup.size - 1) {', content, flags=re.DOTALL)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
