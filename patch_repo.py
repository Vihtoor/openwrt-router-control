import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    lines = f.readlines()

new_lines = []
method_name = "Unknown"
for i, line in enumerate(lines):
    if "fun " in line and "{" in line:
        m = re.search(r'fun ([a-zA-Z0-9_]+)\s*\(', line)
        if m:
            method_name = m.group(1)
            
    new_lines.append(line)
    if "catch (e: Exception) {" in line:
        indent = line[:line.find("catch")] + "    "
        new_lines.append(indent + f'android.util.Log.e("SSH-Debug", "Caught exception in RouterRepository.{method_name}", e)\n')

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.writelines(new_lines)

