import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# First, restore RouterProfilesList
old_rpl = """    val coroutineScope = rememberCoroutineScope()
    val checkConnectionFocusRequester = remember { FocusRequester() }
    if (!isFirst) {
        BackHandler {
            onCancel()
        }
    }
    var isCheckingForUpdate by remember { mutableStateOf(false) }"""

new_rpl = """    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isCheckingForUpdate by remember { mutableStateOf(false) }"""

content = content.replace(old_rpl, new_rpl)

# Next, add to EditRouterForm
old_erf = """    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Card("""

new_erf = """    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val checkConnectionFocusRequester = remember { FocusRequester() }
    if (!isFirst) {
        BackHandler {
            onCancel()
        }
    }

    Card("""

content = content.replace(old_erf, new_erf)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
