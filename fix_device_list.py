with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# 1. Fix list highlighting in DeviceListDialog
target_list_item = """                                    itemsIndexed(devicesInGroup) { index, device ->
                                        var isFocused by remember { mutableStateOf(false) }
                                        val isSelected = focusedDeviceMac == device.mac || (focusedDeviceMac == null && index == 0)
                                        Row(
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
                                                )"""

replacement_list_item = """                                    itemsIndexed(devicesInGroup) { index, device ->
                                        var isFocused by remember { mutableStateOf(false) }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .onFocusChanged { 
                                                    isFocused = it.isFocused
                                                    if (it.isFocused) {
                                                        focusedDeviceMac = device.mac
                                                    }
                                                }
                                                .clickable { focusedDeviceMac = device.mac }
                                                .padding(vertical = 7.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                                .padding(vertical = 5.dp, horizontal = 12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = device.hostname,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isFocused) Color.Red else MaterialTheme.colorScheme.onSurface
                                                )"""

content = content.replace(target_list_item, replacement_list_item)


# 2. Fix Button in right pane
target_button = """                                var btnFocused by remember { mutableStateOf(false) }
                                androidx.compose.material3.Button(
                                    onClick = {
                                        clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                                    },
                                    modifier = Modifier.padding(top = 8.dp).onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent)
                                ) {"""

replacement_button = """                                var btnFocused by remember { mutableStateOf(false) }
                                androidx.compose.material3.Button(
                                    onClick = {
                                        clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                                    },
                                    modifier = Modifier.padding(top = 8.dp).onFocusChanged { btnFocused = it.isFocused },
                                    border = if (btnFocused) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
                                ) {"""

content = content.replace(target_button, replacement_button)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
