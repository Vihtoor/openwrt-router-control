def revert_file(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        content = f.read()
    content = content.replace('.androidx.compose.foundation.verticalScroll(scrollState)', '.verticalScroll(scrollState)')
    content = content.replace('.androidx.compose.foundation.focusable(interactionSource = interactionSource)', '.focusable(interactionSource = interactionSource)')
    with open(filename, 'w', encoding='utf-8') as f:
        f.write(content)

revert_file('app/src/main/java/com/example/ui/SettingsPanel.kt')
revert_file('app/src/main/java/com/example/MainActivity.kt')
print("Reverted")
