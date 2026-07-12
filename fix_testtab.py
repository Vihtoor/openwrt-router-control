import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = re.sub(
    r'                viewModel\.switchTab\(TabType\.CONSOLE\)\n\s+\}\n\s+\)',
    r'                viewModel.switchTab(TabType.CONSOLE)\n            },\n            onRefreshCapabilities = { viewModel.recheckCapabilities() }\n        )',
    content
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
