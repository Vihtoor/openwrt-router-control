with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace('translateText("Температура")', 'translateText("Температура", androidx.compose.ui.platform.LocalContext.current)')
content = content.replace('translateText("Процессор")', 'translateText("Процессор", androidx.compose.ui.platform.LocalContext.current)')
content = content.replace('translateText("недоступна")', 'translateText("недоступна", androidx.compose.ui.platform.LocalContext.current)')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
