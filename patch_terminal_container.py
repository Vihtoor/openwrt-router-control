import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Add gesture state variables
state_vars = """    var currentTypedLine by remember { mutableStateOf("") }
    var showGestureOverlay by remember { mutableStateOf(false) }
    var highlightedDirection by remember { mutableStateOf<String?>(null) }"""

content = content.replace('    var currentTypedLine by remember { mutableStateOf("") }', state_vars)

# Replace the clickable and add pointerInput
old_container = """                .border(
                    width = 2.dp,
                    color = if (isTerminalFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    terminalView?.requestFocus()
                                        terminalView?.let { 
                        imm.restartInput(it)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            it.windowInsetsController?.show(android.view.WindowInsets.Type.ime())
                        } else {
                            imm.showSoftInput(it, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT) 
                        }
                    }
                }
                .padding(12.dp)
        ) {"""

new_container = """                .border(
                    width = 2.dp,
                    color = if (isTerminalFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .pointerInput(Unit) {
                    var accumulatedX = 0f
                    var accumulatedY = 0f
                    val threshold = 70f
                    androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress(
                        onDragStart = { offset ->
                            showGestureOverlay = true
                            highlightedDirection = null
                            accumulatedX = 0f
                            accumulatedY = 0f
                        },
                        onDragEnd = {
                            showGestureOverlay = false
                            highlightedDirection = null
                        },
                        onDragCancel = {
                            showGestureOverlay = false
                            highlightedDirection = null
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            accumulatedX += dragAmount.x
                            accumulatedY += dragAmount.y
                            
                            if (kotlin.math.abs(accumulatedX) > threshold || kotlin.math.abs(accumulatedY) > threshold) {
                                if (kotlin.math.abs(accumulatedX) > kotlin.math.abs(accumulatedY)) {
                                    if (accumulatedX > 0) {
                                        highlightedDirection = "RIGHT"
                                        if (currentTypedLine.isNotEmpty()) {
                                            onWriteRawToConsoleStdin("\\u001B[C")
                                        }
                                    } else {
                                        highlightedDirection = "LEFT"
                                        if (currentTypedLine.isNotEmpty()) {
                                            onWriteRawToConsoleStdin("\\u001B[D")
                                        }
                                    }
                                } else {
                                    if (accumulatedY > 0) {
                                        highlightedDirection = "DOWN"
                                        if (sessionCommandHistory.isNotEmpty()) {
                                            if (historyIndex < sessionCommandHistory.size - 1 && historyIndex != -1) {
                                                historyIndex++
                                                val cmd = sessionCommandHistory[historyIndex]
                                                val bytesToDelete = currentTypedLine.toByteArray(Charsets.UTF_8).size
                                                repeat(bytesToDelete) { onWriteRawToConsoleStdin("\\u007F") }
                                                cmd.forEach { char -> onWriteRawToConsoleStdin(char.toString()) }
                                                currentTypedLine = cmd
                                            } else if (historyIndex == sessionCommandHistory.size - 1) {
                                                historyIndex = -1
                                                val bytesToDelete = currentTypedLine.toByteArray(Charsets.UTF_8).size
                                                repeat(bytesToDelete) { onWriteRawToConsoleStdin("\\u007F") }
                                                currentTypedLine = ""
                                            }
                                        }
                                    } else {
                                        highlightedDirection = "UP"
                                        if (sessionCommandHistory.isNotEmpty()) {
                                            if (historyIndex == -1) {
                                                historyIndex = sessionCommandHistory.size - 1
                                            } else if (historyIndex > 0) {
                                                historyIndex--
                                            }
                                            val cmd = sessionCommandHistory[historyIndex]
                                            val bytesToDelete = currentTypedLine.toByteArray(Charsets.UTF_8).size
                                            repeat(bytesToDelete) { onWriteRawToConsoleStdin("\\u007F") }
                                            cmd.forEach { char -> onWriteRawToConsoleStdin(char.toString()) }
                                            currentTypedLine = cmd
                                        }
                                    }
                                }
                                accumulatedX = 0f
                                accumulatedY = 0f
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    androidx.compose.foundation.gestures.detectTapGestures(
                        onTap = {
                            terminalView?.requestFocus()
                            terminalView?.let {
                                imm.restartInput(it)
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                                    it.windowInsetsController?.show(android.view.WindowInsets.Type.ime())
                                } else {
                                    imm.showSoftInput(it, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
                                }
                            }
                        }
                    )
                }
                .padding(12.dp)
        ) {
            if (showGestureOverlay) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color(0x88000000), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Up",
                            tint = if (highlightedDirection == "UP") Color.Green else Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Row {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Left",
                                tint = if (highlightedDirection == "LEFT") Color.Green else Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Down",
                                tint = if (highlightedDirection == "DOWN") Color.Green else Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "Right",
                                tint = if (highlightedDirection == "RIGHT") Color.Green else Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
"""

if old_container in content:
    content = content.replace(old_container, new_container)
    print("Patched container successfully")
else:
    print("Failed to find container to patch")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
