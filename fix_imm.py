import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

replacement = """                    terminalView?.let { 
                        imm.restartInput(it)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            it.windowInsetsController?.show(android.view.WindowInsets.Type.ime())
                        } else {
                            imm.showSoftInput(it, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT) 
                        }
                    }"""

content = content.replace(
    "terminalView?.let { imm.showSoftInput(it, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT) }",
    replacement
)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
print("Replaced imm.showSoftInput successfully.")
