with open('app/src/main/java/com/example/ui/SettingsPanel.kt', 'r', encoding='utf-8') as f:
    content = f.read()

import re

# EditRouterForm changes
content = content.replace(
    "val isImeVisible = WindowInsets.isImeVisible",
    """val profileNameFocusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    LaunchedEffect(Unit) {
        if (isTv && config.id == 0) {
            try { profileNameFocusRequester.requestFocus() } catch (e: Exception) {}
        }
    }
    val isImeVisible = WindowInsets.isImeVisible"""
)

content = content.replace(
    """modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {""",
    """modifier = Modifier.fillMaxWidth().focusRequester(profileNameFocusRequester),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {"""
)

# RouterProfilesList changes
content = content.replace(
    """val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()""",
    """val isTv = remember { context.packageManager.hasSystemFeature("android.software.leanback") }
    val addRouterFocusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    LaunchedEffect(Unit) {
        if (isTv) {
            try { addRouterFocusRequester.requestFocus() } catch (e: Exception) {}
        }
    }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()"""
)

content = content.replace(
    """Button(
                onClick = onAddNew,
                interactionSource = addInteractionSource,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),""",
    """Button(
                onClick = onAddNew,
                interactionSource = addInteractionSource,
                modifier = Modifier.fillMaxWidth().focusRequester(addRouterFocusRequester),
                shape = RoundedCornerShape(12.dp),"""
)

with open('app/src/main/java/com/example/ui/SettingsPanel.kt', 'w', encoding='utf-8') as f:
    f.write(content)

print("Done")
