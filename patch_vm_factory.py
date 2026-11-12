import re

with open("app/src/main/java/com/example/ui/RouterViewModelFactory.kt", "r") as f:
    content = f.read()

content = content.replace("val sshClientManager = SshClientManager()", "val sshClientManager = SshClientManager(application)")

with open("app/src/main/java/com/example/ui/RouterViewModelFactory.kt", "w") as f:
    f.write(content)
