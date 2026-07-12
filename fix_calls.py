import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Fix DeviceListDialog calls to pass onRefreshCapabilities
content = re.sub(
    r'onNavigateToConsole = \{ cmd ->\n\s+showDevices = false\n\s+onNavigateToConsole\(cmd\)\n\s+\}\n\s+\)',
    r'onNavigateToConsole = { cmd ->\n                    showDevices = false\n                    onNavigateToConsole(cmd)\n                },\n                onRefreshCapabilities = onRefreshCapabilities\n            )',
    content
)

content = re.sub(
    r'onNavigateToConsole = \{ cmd ->\n\s+showDevices = false\n\s+if \(cmd != null\) \{\n\s+viewModel\.setCommandInput\(cmd\)\n\s+\}\n\s+\}\n\s+\)',
    r'onNavigateToConsole = { cmd ->\n                showDevices = false\n                if (cmd != null) {\n                    viewModel.setCommandInput(cmd)\n                }\n            },\n            onRefreshCapabilities = { viewModel.recheckCapabilities() }\n        )',
    content
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
