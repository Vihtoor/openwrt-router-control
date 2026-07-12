with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

bad_upload_col = """                var isDlFocused by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showDownloadDevices = true }
                        .onFocusChanged { isDlFocused = it.isFocused }
                        .focusable()
                        .background(if (isDlFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
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

good_upload_col = """                var isUlFocused by remember { mutableStateOf(false) }
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

content = content.replace(bad_upload_col, good_upload_col)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

