with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

import re

# In DashboardTab
content = re.sub(
    r'onNavigateToConsole: \(\) -> Unit = \{\}',
    r'onNavigateToConsole: (String?) -> Unit = {}',
    content
)

# In InternetStatusCard
content = re.sub(
    r'onNavToConsole: \(\) -> Unit = \{\}',
    r'onNavToConsole: (String?) -> Unit = {}',
    content
)

# In SpeedMeterCard
content = re.sub(
    r'onNavigateToConsole: \(\) -> Unit = \{\}',
    r'onNavigateToConsole: (String?) -> Unit = {}',
    content
)

# In DeviceListDialog
content = re.sub(
    r'onNavigateToConsole: \(\) -> Unit = \{\}',
    r'onNavigateToConsole: (String?) -> Unit = {}',
    content
)

# In InternetStatusCard calling DeviceListDialog
content = content.replace("""                onNavigateToConsole = {
                    showDevices = false
                    onNavToConsole()
                }""", """                onNavigateToConsole = { cmd ->
                    showDevices = false
                    onNavToConsole(cmd)
                }""")

# In DashboardTab calling InternetStatusCard and SpeedMeterCard
content = content.replace("""                        onNavToConsole = onNavigateToConsole""", """                        onNavToConsole = onNavigateToConsole""") # no change needed if the name is the same and they just pass the lambda

# In MainActivity calling DashboardTab
content = content.replace("""                        DashboardTab(
                            state = state,
                            onLedToggle = { viewModel.toggleLed(it) },
                            onMasterVpnToggle = { viewModel.toggleMasterVpn(it) },
                            onOpenVpnListDialog = { viewModel.openVpnListDialog() },
                            onToggleTentativeVpnItem = { name, enabled -> viewModel.toggleTentativeVpnItem(name, enabled) },
                            onApplyVpnChanges = { viewModel.applyVpnChanges() },
                            onCancelVpnChanges = { viewModel.cancelVpnChanges() },
                            onOpenDnsListDialog = { viewModel.openDnsListDialog() },
                            onDnsVariantSelected = { viewModel.applyDnsChanges(it) },
                            onCancelDnsChanges = { viewModel.cancelDnsChanges() },
                            onRefreshStatus = { viewModel.refreshStatus() },
                            onNavigateToConsole = { viewModel.switchTab(TabType.CONSOLE) }
                        )""", """                        DashboardTab(
                            state = state,
                            onLedToggle = { viewModel.toggleLed(it) },
                            onMasterVpnToggle = { viewModel.toggleMasterVpn(it) },
                            onOpenVpnListDialog = { viewModel.openVpnListDialog() },
                            onToggleTentativeVpnItem = { name, enabled -> viewModel.toggleTentativeVpnItem(name, enabled) },
                            onApplyVpnChanges = { viewModel.applyVpnChanges() },
                            onCancelVpnChanges = { viewModel.cancelVpnChanges() },
                            onOpenDnsListDialog = { viewModel.openDnsListDialog() },
                            onDnsVariantSelected = { viewModel.applyDnsChanges(it) },
                            onCancelDnsChanges = { viewModel.cancelDnsChanges() },
                            onRefreshStatus = { viewModel.refreshStatus() },
                            onNavigateToConsole = { cmd -> 
                                if (cmd != null) {
                                    viewModel.setCommandInput(cmd)
                                }
                                viewModel.switchTab(TabType.CONSOLE) 
                            }
                        )""")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
