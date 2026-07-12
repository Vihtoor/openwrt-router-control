import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Add mutableStateOf to SpeedMeterCard
if "var showDownloadDevices by remember { mutableStateOf(false) }" not in content:
    content = content.replace("var isFocused by remember { mutableStateOf(false) }", "var isFocused by remember { mutableStateOf(false) }\n    var showDownloadDevices by remember { mutableStateOf(false) }\n    var showUploadDevices by remember { mutableStateOf(false) }")

# Make Download Column clickable
download_col_old = """                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {"""
download_col_new = """                var isDlFocused by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showDownloadDevices = true }
                        .onFocusChanged { isDlFocused = it.isFocused }
                        .focusable()
                        .background(if (isDlFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {"""
content = content.replace(download_col_old, download_col_new)

# Make Upload Column clickable
upload_col_old = """                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFFFB300))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Выгрузка ↑","""

upload_col_new = """                var isUlFocused by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showUploadDevices = true }
                        .onFocusChanged { isUlFocused = it.isFocused }
                        .focusable()
                        .background(if (isUlFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFFFB300))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Выгрузка ↑","""
content = content.replace(upload_col_old, upload_col_new)

# Add Dialogs at the end of SpeedMeterCard
dialogs_code = """
        if (showDownloadDevices) {
            DeviceListDialog(
                title = "Устройства (Загрузка ↓)",
                devices = state.deviceSpeeds,
                isDownload = true,
                onDismiss = { showDownloadDevices = false }
            )
        }
        if (showUploadDevices) {
            DeviceListDialog(
                title = "Устройства (Выгрузка ↑)",
                devices = state.deviceSpeeds,
                isDownload = false,
                onDismiss = { showUploadDevices = false }
            )
        }
"""
if "DeviceListDialog(" not in content:
    content = content.replace("    }\n}\n\nfun Modifier.drawSimpleScrollbar(", dialogs_code + "    }\n}\n\nfun Modifier.drawSimpleScrollbar(")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
