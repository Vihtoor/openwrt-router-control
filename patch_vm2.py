import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

replacement = """        android.util.Log.d("RouterViewModel", "testConnection called with sshKeyId=$sshKeyId")
        viewModelScope.launch {
            _uiState.update { it.copy(isConnecting = true, connectionError = null) }
            val cleanedIp = cleanHost(ip)
            android.util.Log.d("RouterViewModel", "config BEFORE suspect operation: sshKeyId=$sshKeyId")
            val tempConfig = RouterConfig(
                ipAddress = cleanedIp,
                port = port,
                username = user.trim(),
                sshKeyOrPassword = pass
            )
            android.util.Log.d("RouterViewModel", "config AFTER suspect operation: sshKeyId=${tempConfig.sshKeyId}")"""

content = content.replace("""        android.util.Log.d("RouterViewModel", "testConnection called with sshKeyId=$sshKeyId")
        viewModelScope.launch {
            _uiState.update { it.copy(isConnecting = true, connectionError = null) }
            val cleanedIp = cleanHost(ip)
            val tempConfig = RouterConfig(
                ipAddress = cleanedIp,
                port = port,
                username = user.trim(),
                sshKeyOrPassword = pass
            )""", replacement)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
