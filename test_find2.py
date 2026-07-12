import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """        androidx.compose.ui.window.Dialog(
            onDismissRequest = onDismiss,
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val listBg = MaterialTheme.colorScheme.surface
            val headerBg = androidx.compose.ui.graphics.lerp(listBg, Color.White, 0.08f)

            Card(
                modifier = Modifier
                    .width(360.dp)
                    .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.systemBars)
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = listBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {"""

print(target in content)
