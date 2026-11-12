import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
    'val telemetry = repository.queryWanTrafficBytes(config)',
    'android.util.Log.d("SSH-Poller", "config.sshKeyId=${config.sshKeyId}, source=RouterViewModel.speedJob");\n                    val telemetry = repository.queryWanTrafficBytes(config)'
)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/RouterWidgetProvider.kt", "r") as f:
    content = f.read()

content = content.replace(
    'val status = repository.queryRouterStatus(config)',
    'android.util.Log.d("SSH-Poller", "config.sshKeyId=${config.sshKeyId}, source=RouterWidgetProvider");\n                    val status = repository.queryRouterStatus(config)'
)

with open("app/src/main/java/com/example/RouterWidgetProvider.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/RouterLedTileService.kt", "r") as f:
    content = f.read()

content = content.replace(
    'val status = repository.queryRouterStatus(config)',
    'android.util.Log.d("SSH-Poller", "config.sshKeyId=${config.sshKeyId}, source=RouterLedTileService");\n                val status = repository.queryRouterStatus(config)'
)

with open("app/src/main/java/com/example/RouterLedTileService.kt", "w") as f:
    f.write(content)

