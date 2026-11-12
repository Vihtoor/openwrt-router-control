with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
'''                                    val image = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility''',
'''                                    val image = if (passwordVisible) Icons.Default.Clear else Icons.Default.Lock'''
)

with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'w', encoding='utf-8') as f:
    f.write(content)

print("Done")
