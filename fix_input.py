import re

with open('app/src/main/java/com/example/ui/TerminalInputView.kt', 'r') as f:
    content = f.read()

# 1. Add outAttrs fields and change to fullEditor=true
content = content.replace(
    'outAttrs.imeOptions = EditorInfo.IME_ACTION_NONE or EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN',
    'outAttrs.imeOptions = EditorInfo.IME_ACTION_NONE or EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN\n        outAttrs.initialSelStart = 0\n        outAttrs.initialSelEnd = 0'
)
content = content.replace(
    'return object : BaseInputConnection(this, false) {',
    'return object : BaseInputConnection(this, true) {'
)

# 2. Add logs to commitText and sendKeyEvent
content = content.replace(
    'override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {\n                if (composingText.isNotEmpty()) {',
    'override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {\n                android.util.Log.d("TermFocus", "commitText: $text")\n                if (composingText.isNotEmpty()) {'
)
content = content.replace(
    'override fun sendKeyEvent(event: KeyEvent): Boolean {\n                val handled = onSendKeyEvent(event)',
    'override fun sendKeyEvent(event: KeyEvent): Boolean {\n                android.util.Log.d("TermFocus", "sendKeyEvent: ${event.keyCode}")\n                val handled = onSendKeyEvent(event)'
)

# 3. Remove restartInput
content = content.replace(
    '                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager\n                imm.restartInput(this@TerminalInputView)\n                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {',
    '                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager\n                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {'
)

with open('app/src/main/java/com/example/ui/TerminalInputView.kt', 'w') as f:
    f.write(content)
print("Replaced successfully")
