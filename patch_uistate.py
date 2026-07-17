import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

replacement = """data class UiState(
    val interactiveCursorIndex: Int = 0,"""
content = content.replace("data class UiState(", replacement)

# Update state in RouterViewModel when cursor index changes
# We can just update it in the viewModelScope.launch at the end of startInteractiveShell callback
update_block_target = """                    viewModelScope.launch {
                        interactiveShellLogId?.let { lid ->
                            repository.updateConsoleLog(lid, "sh", interactiveShellOutputBuffer.toString())
                        }
                    }"""
update_block_replacement = """                    viewModelScope.launch {
                        _uiState.update { it.copy(interactiveCursorIndex = interactiveCursorIndex) }
                        interactiveShellLogId?.let { lid ->
                            repository.updateConsoleLog(lid, "sh", interactiveShellOutputBuffer.toString())
                        }
                    }"""
content = content.replace(update_block_target, update_block_replacement)

# also set it to 0 when starting shell
start_target = """                interactiveShellOutputBuffer.setLength(0)
                interactiveCursorIndex = 0"""
start_replacement = """                interactiveShellOutputBuffer.setLength(0)
                interactiveCursorIndex = 0
                _uiState.update { it.copy(interactiveCursorIndex = 0) }"""
content = content.replace(start_target, start_replacement)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
