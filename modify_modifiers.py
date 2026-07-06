import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# RouterProfilesList signature
content = content.replace("fun RouterProfilesList(\n    configs: List<RouterConfig>,", "fun RouterProfilesList(\n    modifier: Modifier = Modifier,\n    configs: List<RouterConfig>,")
# RouterProfilesList Card
content = content.replace("    Card(\n        modifier = Modifier\n            .fillMaxWidth()\n            .padding(16.dp),\n        shape = RoundedCornerShape(24.dp),", "    Card(\n        modifier = modifier\n            .fillMaxWidth()\n            .padding(16.dp),\n        shape = RoundedCornerShape(24.dp),")

# EditRouterForm signature
content = content.replace("fun EditRouterForm(\n    config: RouterConfig,", "fun EditRouterForm(\n    modifier: Modifier = Modifier,\n    config: RouterConfig,")
# EditRouterForm Card
content = content.replace("    Card(\n        modifier = Modifier\n            .fillMaxWidth()\n            .padding(16.dp)\n            .imePadding(),\n        shape = RoundedCornerShape(24.dp),", "    Card(\n        modifier = modifier\n            .fillMaxWidth()\n            .padding(16.dp)\n            .imePadding(),\n        shape = RoundedCornerShape(24.dp),")

# SettingsPanel calls
content = content.replace("        EditRouterForm(\n            config = configToEdit,", "        EditRouterForm(\n            modifier = if (isTablet) Modifier.fillMaxHeight() else Modifier,\n            config = configToEdit,")
content = content.replace("        RouterProfilesList(\n            configs = state.allConfigs,", "        RouterProfilesList(\n            modifier = if (isTablet) Modifier.fillMaxHeight() else Modifier,\n            configs = state.allConfigs,")

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
