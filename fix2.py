with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

new_dialog = """
        if (showDevices) {
            DeviceListDialog(
                title = androidx.compose.ui.res.stringResource(R.string.devices_title),
                config = state.config,
                devices = state.deviceSpeeds,
                deviceHistory = state.deviceSpeedHistory,
                onDismiss = { showDevices = false }
            )
        }
    }
}
"""

content = content.replace("                    }\n}\n\nfun Modifier.drawSimpleScrollbar(", new_dialog + "\nfun Modifier.drawSimpleScrollbar(")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
