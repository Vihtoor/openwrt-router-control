import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# 1. Pass interactiveCursorIndex to ConsoleTab
call_target = """                        TabType.CONSOLE -> ConsoleTab(
                            consoleHistory = state.consoleHistory.filter { !it.command.contains("iperf3") },"""
call_replace = """                        TabType.CONSOLE -> ConsoleTab(
                            interactiveCursorIndex = state.interactiveCursorIndex,
                            consoleHistory = state.consoleHistory.filter { !it.command.contains("iperf3") },"""
content = content.replace(call_target, call_replace)

# 2. Add interactiveCursorIndex to ConsoleTab arguments
def_target = """fun ConsoleTab(
    consoleHistory: List<com.example.data.ConsoleLog>,"""
def_replace = """fun ConsoleTab(
    interactiveCursorIndex: Int = 0,
    consoleHistory: List<com.example.data.ConsoleLog>,"""
content = content.replace(def_target, def_replace)

# 3. Modify rendering of finalOutput
render_target = """                                                val parsedOutput = androidx.compose.runtime.remember(log.output) {
                                                    parseAnsiToAnnotatedString(log.output)
                                                }
                                                val isMostRecentSh = index == 0 && log.command == "sh"
                                                val finalOutput = if (isMostRecentSh && cursorVisible) {
                                                    androidx.compose.ui.text.buildAnnotatedString {
                                                        append(parsedOutput)
                                                        append("█")
                                                    }
                                                } else {
                                                    parsedOutput
                                                }"""
render_replace = """                                                val isMostRecentSh = index == 0 && log.command == "sh"
                                                val outputWithCursor = if (isMostRecentSh && cursorVisible) {
                                                    if (interactiveCursorIndex in 0..log.output.length) {
                                                        log.output.substring(0, interactiveCursorIndex) + "█" + log.output.substring(interactiveCursorIndex)
                                                    } else {
                                                        log.output + "█"
                                                    }
                                                } else {
                                                    log.output
                                                }
                                                val finalOutput = androidx.compose.runtime.remember(outputWithCursor) {
                                                    parseAnsiToAnnotatedString(outputWithCursor)
                                                }"""
content = content.replace(render_target, render_replace)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
