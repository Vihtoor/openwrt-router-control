import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

old_onselect = """            onSelect = { id ->
                if (state.config?.id != id) {
                    viewModel.switchConfig(id)
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        val intent = android.content.Intent(context, com.example.MainActivity::class.java)
                        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                        Runtime.getRuntime().exit(0)
                    }, 300)
                } else {
                    onDismiss()
                }
            },"""

new_onselect = """            onSelect = { id ->
                if (state.config?.id != id) {
                    viewModel.switchConfig(id) {
                        val intent = android.content.Intent(context, com.example.MainActivity::class.java)
                        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                        Runtime.getRuntime().exit(0)
                    }
                } else {
                    onDismiss()
                }
            },"""

content = content.replace(old_onselect, new_onselect)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
