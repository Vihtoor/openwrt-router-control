with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    lines = f.readlines()

new_lines = []
skip = False
for line in lines:
    if "if (showDevices) {" in line and "ConsoleTab" not in line:
        # Check if it's inside ConsoleTab by some line number approximation
        pass
        
    new_lines.append(line)
    
# Let's use regex instead:
with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

import re

# Remove the bad injection in ConsoleTab
bad_dialog = r'        if \(showDevices\) \{\s+DeviceListDialog\(\s+title = androidx.compose.ui.res.stringResource\(R.string.devices_title\),\s+config = state.config,\s+devices = state.deviceSpeeds,\s+deviceHistory = state.deviceSpeedHistory,\s+onDismiss = \{ showDevices = false \}\s+\)\s+\}\s+'
content = re.sub(bad_dialog, '', content)

# Remove isDownload from DeviceListDialog
content = re.sub(r'val speed = if \(isDownload\) device.downloadSpeedMbps else device.uploadSpeedMbps\n', '', content)
content = re.sub(r'val totalBytes = if \(isDownload\) device.downloadBytes else device.uploadBytes\n', '', content)
content = re.sub(r'val speedColor = if \(isDownload\) \{\n.*?\n.*?\n.*?\n', '', content, flags=re.DOTALL)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
