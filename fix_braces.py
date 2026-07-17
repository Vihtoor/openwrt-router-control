import re

file_path = "app/src/main/java/com/example/MainActivity.kt"
with open(file_path, "r") as f:
    lines = f.readlines()

for i, line in enumerate(lines):
    if "confirmButton = {" in line:
        # print previous 5 lines
        print("Line", i)
        for j in range(max(0, i-5), i):
            print(lines[j].rstrip())
        print(line.rstrip())
        print("-" * 20)
