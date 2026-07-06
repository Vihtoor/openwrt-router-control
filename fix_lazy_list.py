import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# RouterProfilesList block to replace
old_router_profiles_list = """fun RouterProfilesList(
    modifier: Modifier = Modifier,
    configs: List<RouterConfig>,
    activeConfigId: Int?,
    onSelect: (Int) -> Unit,
    onEdit: (RouterConfig) -> Unit,
    onDelete: (Int) -> Unit,
    onAddNew: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var showAboutDialog by remember { mutableStateOf(false) }
    var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }
    val listState = rememberLazyListState()
    val isImeVisible = WindowInsets.isImeVisible
    LaunchedEffect(isImeVisible) {
        if (isImeVisible && isPasswordFocused) {
            kotlinx.coroutines.delay(200)
            listState.animateScrollBy(10000f)
        }
    }"""
new_router_profiles_list = """fun RouterProfilesList(
    modifier: Modifier = Modifier,
    configs: List<RouterConfig>,
    activeConfigId: Int?,
    onSelect: (Int) -> Unit,
    onEdit: (RouterConfig) -> Unit,
    onDelete: (Int) -> Unit,
    onAddNew: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var showAboutDialog by remember { mutableStateOf(false) }
    var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }
    val listState = rememberLazyListState()"""

content = content.replace(old_router_profiles_list, new_router_profiles_list)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
