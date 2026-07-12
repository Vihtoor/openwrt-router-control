import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

cmd_match = re.search(r'val cmd = "(.*?)".*?return try', content, re.DOTALL)
if cmd_match:
    print(cmd_match.group(1))
