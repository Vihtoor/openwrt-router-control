with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target_focus_req = """            val primaryColor = MaterialTheme.colorScheme.primary
            val dialogFocusRequester = remember { FocusRequester() }
            var isDialogColumnFocused by remember { mutableStateOf(false) }"""

replacement_focus_req = """            val primaryColor = MaterialTheme.colorScheme.primary
            val dialogFocusRequester = remember { FocusRequester() }
            val copyButtonFocusRequester = remember { FocusRequester() }
            var isDialogColumnFocused by remember { mutableStateOf(false) }"""

content = content.replace(target_focus_req, replacement_focus_req)

target_on_key = """                                                     Key.DirectionDown -> {
                                                         if (scrollState.value < scrollState.maxValue) {
                                                             scope.launch {
                                                                 scrollState.animateScrollBy(150f)
                                                             }
                                                             true
                                                         } else {
                                                             false
                                                         }
                                                     }"""

replacement_on_key = """                                                     Key.DirectionDown -> {
                                                         if (scrollState.value < scrollState.maxValue) {
                                                             scope.launch {
                                                                 scrollState.animateScrollBy(150f)
                                                             }
                                                             true
                                                         } else {
                                                             try {
                                                                 copyButtonFocusRequester.requestFocus()
                                                                 true
                                                             } catch (e: Exception) {
                                                                 false
                                                             }
                                                         }
                                                     }"""

content = content.replace(target_on_key, replacement_on_key)

target_btn = """                                    var btnFocused by remember { mutableStateOf(false) }
                                    androidx.compose.material3.IconButton(
                                        onClick = {
                                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(paragraph))
                                        },
                                        modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                                    ) {"""

replacement_btn = """                                    var btnFocused by remember { mutableStateOf(false) }
                                    androidx.compose.material3.IconButton(
                                        onClick = {
                                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(paragraph))
                                        },
                                        modifier = Modifier.focusRequester(copyButtonFocusRequester).onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                                    ) {"""

content = content.replace(target_btn, replacement_btn)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
