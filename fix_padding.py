with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    lines = f.readlines()

for i in range(len(lines)):
    if "padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 16.dp)" in lines[i]:
        if i + 1 != 6322: # 1-based index is 6322, so i == 6321
            lines[i] = lines[i].replace("padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 16.dp)", "padding(16.dp)")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.writelines(lines)
