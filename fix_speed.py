import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_speed = """                                        Text(
                                            text = String.format("%.2f Мбит/с", speed),
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = speedColor
                                        )"""

new_speed = """                                        val totalBytes = if (isDownload) device.downloadBytes else device.uploadBytes
                                        val totalBytesStr = if (totalBytes < 1024 * 1024) {
                                            String.format("%.1f КБ", totalBytes / 1024f)
                                        } else if (totalBytes < 1024 * 1024 * 1024) {
                                            String.format("%.1f МБ", totalBytes / (1024f * 1024f))
                                        } else {
                                            String.format("%.1f ГБ", totalBytes / (1024f * 1024f * 1024f))
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = String.format("%.2f Мбит/с", speed),
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = speedColor
                                            )
                                            Text(
                                                text = totalBytesStr,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }"""

content = content.replace(old_speed, new_speed)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
