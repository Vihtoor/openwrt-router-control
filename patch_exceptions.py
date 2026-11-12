import re

def replace_in_file(filename):
    with open(filename, "r") as f:
        content = f.read()

    # Replace Log.e("...", "...") with Log.e("...", "...", e)
    content = re.sub(
        r'(Log\.e\([^,]+,\s*"[^"]+"\))',
        r'\1 /* PATCHED */', # Just to see what we match
        content
    )

    with open(filename, "w") as f:
        f.write(content)

replace_in_file("app/src/main/java/com/example/data/RouterRepository.kt")

