import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Add isTv parameter to EditRouterForm
old_erf_sig = """fun EditRouterForm(
    modifier: Modifier = Modifier,
    config: RouterConfig,
    isFirst: Boolean,"""
new_erf_sig = """fun EditRouterForm(
    modifier: Modifier = Modifier,
    config: RouterConfig,
    isFirst: Boolean,
    isTv: Boolean,"""
content = content.replace(old_erf_sig, new_erf_sig)

old_erf_call = """        EditRouterForm(
            modifier = if (isTablet) Modifier.fillMaxSize() else Modifier,
            config = configToEdit,
            isFirst = state.allConfigs.isEmpty(),"""
new_erf_call = """        EditRouterForm(
            modifier = if (isTablet) Modifier.fillMaxSize() else Modifier,
            config = configToEdit,
            isFirst = state.allConfigs.isEmpty(),
            isTv = isTv,"""
content = content.replace(old_erf_call, new_erf_call)

# Add spacer for TV
old_spacer = """            item {
                Button(
                    onClick = {
                        val p = port.toIntOrNull() ?: 22
                        // "wgInterface" defaults to wg0 here, ledBehavior defaults to always_on
                        onSave(profileName, ip, p, user, password, config.ledBehavior, config.wgInterface)
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(translateText("Сохранить профиль", context))
                }
            }
        }"""
new_spacer = """            item {
                Button(
                    onClick = {
                        val p = port.toIntOrNull() ?: 22
                        // "wgInterface" defaults to wg0 here, ledBehavior defaults to always_on
                        onSave(profileName, ip, p, user, password, config.ledBehavior, config.wgInterface)
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(translateText("Сохранить профиль", context))
                }
            }
            if (isTv) {
                item {
                    Spacer(modifier = Modifier.height(250.dp))
                }
            }
        }"""
content = content.replace(old_spacer, new_spacer)

# Make restart code use makeRestartActivityTask
old_restart = """                        val intent = android.content.Intent(context, com.example.MainActivity::class.java)
                        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                        Runtime.getRuntime().exit(0)"""
new_restart = """                        val packageManager = context.packageManager
                        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
                        val componentName = intent!!.component
                        val mainIntent = android.content.Intent.makeRestartActivityTask(componentName)
                        context.startActivity(mainIntent)
                        Runtime.getRuntime().exit(0)"""
content = content.replace(old_restart, new_restart)

# Add BackHandler to RouterProfilesList
old_rpl = """    var isCheckingForUpdate by remember { mutableStateOf(false) }

    if (showAboutDialog) {"""
new_rpl = """    var isCheckingForUpdate by remember { mutableStateOf(false) }

    BackHandler {
        onDismiss()
    }

    if (showAboutDialog) {"""
content = content.replace(old_rpl, new_rpl)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
