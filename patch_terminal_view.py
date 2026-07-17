import re

with open("app/src/main/java/com/example/ui/TerminalInputView.kt", "r") as f:
    content = f.read()

content = re.sub(r'private val gestureDetector.*?\}\)\n', '', content, flags=re.DOTALL)
content = re.sub(r'override fun onTouchEvent\(event: android\.view\.MotionEvent\): Boolean \{\n\s*gestureDetector\.onTouchEvent\(event\)\n\s*return super\.onTouchEvent\(event\)\n\s*\}\n', '', content)

with open("app/src/main/java/com/example/ui/TerminalInputView.kt", "w") as f:
    f.write(content)
