with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    lines = f.readlines()

new_lines = []
skip = False
for line in lines:
    if "val candidates = activeMacs.filter" in line or 'android.util.Log.d("EthernetDebug"' in line:
        continue
    new_lines.append(line)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.writelines(new_lines)
