import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r", encoding="utf-8") as f:
    content = f.read()

old_switch = """    fun switchConfig(id: Int) {
        val targetConfig = _uiState.value.allConfigs.find { it.id == id } ?: return
        viewModelScope.launch {
            stopPollers()
            val updatedConfig = targetConfig.copy(isActive = true)
            repository.saveRouterConfig(updatedConfig)
        }
    }"""

new_switch = """    fun switchConfig(id: Int, onComplete: () -> Unit = {}) {
        val targetConfig = _uiState.value.allConfigs.find { it.id == id } ?: return
        viewModelScope.launch {
            stopPollers()
            val updatedConfig = targetConfig.copy(isActive = true)
            repository.saveRouterConfig(updatedConfig)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                onComplete()
            }
        }
    }"""

content = content.replace(old_switch, new_switch)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w", encoding="utf-8") as f:
    f.write(content)
