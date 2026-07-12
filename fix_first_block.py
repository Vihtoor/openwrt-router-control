import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# We need to replace the content of the first Row in isTablet branch
# Let's target the exact string block we want to replace
target_block = """                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { focusedDeviceMac = device.mac }
                                                .onFocusChanged { 
                                                    isFocused = it.isFocused
                                                    if (it.isFocused) {
                                                        focusedDeviceMac = device.mac
                                                    }
                                                }
                                                .focusable()
                                                .padding(vertical = 7.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isFocused || isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                                .padding(vertical = 5.dp, horizontal = 12.dp),
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
                                                Text("IP: ${device.ip} • MAC: ${device.mac}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                                                                                                                    }
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
                                        }"""

replacement_block = """                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { focusedDeviceMac = device.mac }
                                                .onFocusChanged { 
                                                    isFocused = it.isFocused
                                                    if (it.isFocused) {
                                                        focusedDeviceMac = device.mac
                                                    }
                                                }
                                                .focusable()
                                                .padding(vertical = 7.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isFocused || isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                                .padding(vertical = 5.dp, horizontal = 12.dp),
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
                                        }"""

if target_block in content:
    content = content.replace(target_block, replacement_block)
    print("Replaced first block perfectly!")
else:
    print("Could not find exact block, using regex!")
    pattern = r'Row\(\s*modifier = Modifier\s*\.fillMaxWidth\(\)\s*\.clickable \{ focusedDeviceMac = device\.mac \}.*?\}\n\s*\}'
    # we just need a robust replacement
    
with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
