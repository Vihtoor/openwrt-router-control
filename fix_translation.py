with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
'''val description = if (passwordVisible) "Скрыть пароль" else "Показать пароль"''',
'''val description = translateText(if (passwordVisible) "Скрыть пароль" else "Показать пароль", context)'''
)

with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'w', encoding='utf-8') as f:
    f.write(content)

print("Done")
