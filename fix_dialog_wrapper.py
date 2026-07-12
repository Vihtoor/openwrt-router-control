import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_card = """        androidx.compose.ui.window.Dialog(
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

new_card = """        androidx.compose.ui.window.Dialog(
            onDismissRequest = onDismiss,
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val listBg = MaterialTheme.colorScheme.surface
            val headerBg = androidx.compose.ui.graphics.lerp(listBg, Color.White, 0.08f)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.systemBars)
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.width(360.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = listBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {"""

content = content.replace(old_card, new_card)

old_end = """                        }
                    }
                }
            }
        }
    }
}"""

new_end = """                        }
                    }
                }
            }
            }
        }
    }
}"""

# Need a safer way to replace the end brackets.
# Let's just find the function end.
