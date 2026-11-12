with open("app/src/main/java/com/example/ui/SshKeyManagerDialog.kt", "r") as f:
    content = f.read()

content = content.replace("androidx.compose.material.icons.filled.Share", "Icons.Default.Share")

with open("app/src/main/java/com/example/ui/SshKeyManagerDialog.kt", "w") as f:
    f.write(content)
