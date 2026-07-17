import re

file_path = "app/src/main/java/com/example/ui/RouterViewModel.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """    // Manual status refresh triggered from the top-right button
    fun refreshStatus() {
        val config = _uiState.value.config ?: return
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isStatusRefreshing = true,
                    vpnList = emptyList(),
                    tentativeVpnList = emptyList(),
                    status = it.status.copy()
                ) 
            }"""

replacement = """    // Manual status refresh triggered from the top-right button
    fun refreshStatus() {
        val config = _uiState.value.config ?: return
        
        _uiState.update { 
            it.copy(
                isStatusRefreshing = true,
                vpnList = emptyList(),
                tentativeVpnList = emptyList(),
                status = it.status.copy()
            ) 
        }
        
        viewModelScope.launch {"""

if target in content:
    content = content.replace(target, replacement)
    with open(file_path, "w") as f:
        f.write(content)
    print("Patch refreshStatus applied")
else:
    print("Target not found")
