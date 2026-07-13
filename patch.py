import sys

def patch_file(filename, old_str, new_str):
    with open(filename, 'r') as f:
        content = f.read()
    
    if old_str not in content:
        print(f"Error: Target content not found in {filename}")
        sys.exit(1)
        
    new_content = content.replace(old_str, new_str)
    
    with open(filename, 'w') as f:
        f.write(new_content)
    print(f"Successfully patched {filename}")

main_activity_old = r"""                    view.onDeleteSurroundingText = { before, _ ->
                        repeat(before) {
                            onWriteRawToConsoleStdin("\u007F")
                        }
                        if (currentTypedLine.isNotEmpty()) {
                            val actualBefore = minOf(before, currentTypedLine.length)
                            currentTypedLine = currentTypedLine.dropLast(actualBefore)
                        }
                    }"""

main_activity_new = r"""                    view.onDeleteSurroundingText = { before, _ ->
                        val textToDelete = currentTypedLine.takeLast(before)
                        val bytesToDelete = textToDelete.toByteArray(Charsets.UTF_8).size
                        repeat(bytesToDelete) {
                            onWriteRawToConsoleStdin("\u007F")
                        }
                        if (currentTypedLine.isNotEmpty()) {
                            val actualBefore = minOf(before, currentTypedLine.length)
                            currentTypedLine = currentTypedLine.dropLast(actualBefore)
                        }
                    }"""

router_vm_old = r"""                            if (isErasePattern) {
                                if (interactiveShellOutputBuffer.isNotEmpty()) {
                                    interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                                }
                                i += 3
                            } else {
                                if (interactiveShellOutputBuffer.isNotEmpty()) {
                                    interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                                }
                                i++
                            }"""

router_vm_new = r"""                            if (isErasePattern) {
                                if (pendingEraseSkips > 0) {
                                    pendingEraseSkips--
                                } else {
                                    if (interactiveShellOutputBuffer.isNotEmpty()) {
                                        val lastChar = interactiveShellOutputBuffer.last()
                                        val bytes = lastChar.toString().toByteArray(Charsets.UTF_8).size
                                        if (bytes > 1) {
                                            pendingEraseSkips += (bytes - 1)
                                        }
                                        interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                                    }
                                }
                                i += 3
                            } else {
                                if (pendingEraseSkips > 0) {
                                    pendingEraseSkips--
                                } else {
                                    if (interactiveShellOutputBuffer.isNotEmpty()) {
                                        val lastChar = interactiveShellOutputBuffer.last()
                                        val bytes = lastChar.toString().toByteArray(Charsets.UTF_8).size
                                        if (bytes > 1) {
                                            pendingEraseSkips += (bytes - 1)
                                        }
                                        interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                                    }
                                }
                                i++
                            }"""

router_vm_old_init = r"""    private var interactiveShellLogId: Long? = null
    private val interactiveShellOutputBuffer = StringBuilder()
    private var interactiveShellJob: Job? = null

    fun startInteractiveShellSession() {"""

router_vm_new_init = r"""    private var interactiveShellLogId: Long? = null
    private val interactiveShellOutputBuffer = StringBuilder()
    private var interactiveShellJob: Job? = null
    private var pendingEraseSkips = 0

    fun startInteractiveShellSession() {"""

router_vm_old_reset = r"""                interactiveShellOutputBuffer.setLength(0)
                val initialText = "Connecting to SSH interactive shell...\r\n" """

router_vm_new_reset = r"""                interactiveShellOutputBuffer.setLength(0)
                pendingEraseSkips = 0
                val initialText = "Connecting to SSH interactive shell...\r\n" """

# Remove trailing spaces
router_vm_old_reset = router_vm_old_reset[:-1]
router_vm_new_reset = router_vm_new_reset[:-1]

patch_file('app/src/main/java/com/example/MainActivity.kt', main_activity_old, main_activity_new)
patch_file('app/src/main/java/com/example/ui/RouterViewModel.kt', router_vm_old, router_vm_new)
patch_file('app/src/main/java/com/example/ui/RouterViewModel.kt', router_vm_old_init, router_vm_new_init)
patch_file('app/src/main/java/com/example/ui/RouterViewModel.kt', router_vm_old_reset, router_vm_new_reset)

