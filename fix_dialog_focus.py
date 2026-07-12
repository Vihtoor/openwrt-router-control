with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Let's check how many times the help dialog is structured this way.
# It's one Help Dialog composable handling all keys.
