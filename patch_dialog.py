import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(translateText("Найдена новая версия:", context) + " ${info.version}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                        item {"""

replacement = """                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(translateText("Найдена новая версия:", context) + " ${info.version}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val scrollState = androidx.compose.foundation.rememberScrollState()
                    Column(modifier = Modifier.weight(1f, fill = false).androidx.compose.foundation.verticalScroll(scrollState)) {"""

content = content.replace(target, replacement)

target2 = """                        }
                    }
                }
            },
            confirmButton = {"""

replacement2 = """                    }
                }
            },
            confirmButton = {"""

content = content.replace(target2, replacement2)

with open(file_path, "w") as f:
    f.write(content)
print("Done")
