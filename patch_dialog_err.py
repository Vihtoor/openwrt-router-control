with open("app/src/main/java/com/example/ui/SshKeyManagerDialog.kt", "r") as f:
    content = f.read()

content = content.replace('catch (e: Exception) {', 'catch (e: Throwable) {')
content = content.replace('Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()', 'Toast.makeText(context, "Error: ${e.message ?: e.toString()}", Toast.LENGTH_LONG).show()')

with open("app/src/main/java/com/example/ui/SshKeyManagerDialog.kt", "w") as f:
    f.write(content)
