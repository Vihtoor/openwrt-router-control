import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Add isValidOpenVpnProcessLine function
helper_func = """
    private fun isValidOpenVpnProcessLine(line: String): Boolean {
        val lower = line.lowercase()
        return lower.contains("openvpn") && 
            !lower.contains("grep") && 
            !lower.contains("echo") && 
            !lower.contains("ps |") &&
            !lower.contains("sh -c") &&
            !lower.contains("bin/sh") &&
            !lower.contains("ps ") &&
            !lower.contains("uci ") &&
            !lower.contains("export path=") &&
            !lower.contains("/etc/init.d/openvpn") &&
            !lower.contains("logread") &&
            !lower.contains("tail ") &&
            !lower.contains("cat ") &&
            !lower.contains("vi ") &&
            !lower.contains("nano ") &&
            !lower.contains("awk ") &&
            !lower.contains("sed ")
    }
"""

# Insert before extractSection
content = content.replace("    private fun extractSection(input: String, startTag: String, endTag: String?): String {", helper_func.lstrip() + "\n    private fun extractSection(input: String, startTag: String, endTag: String?): String {")


old_is_active_1 = """            val isOpenVpnActive = openVpnPart.lines().any { line ->
                val lower = line.lowercase()
                lower.contains("openvpn") && 
                !lower.contains("grep") && 
                !lower.contains("echo") && 
                !lower.contains("ps |") &&
                !lower.contains("sh -c") &&
                !lower.contains("bin/sh") &&
                !lower.contains("ps ") &&
                !lower.contains("uci ") &&
                !lower.contains("export path=")
            }"""

new_is_active_1 = """            val isOpenVpnActive = openVpnPart.lines().any { line ->
                isValidOpenVpnProcessLine(line)
            }"""
content = content.replace(old_is_active_1, new_is_active_1)


old_is_active_2 = """                    res.stdout.lines().any { line ->
                        val lower = line.lowercase()
                        lower.contains("openvpn") && 
                        !lower.contains("grep") && 
                        !lower.contains("echo") && 
                        !lower.contains("ps |") &&
                        !lower.contains("sh -c") &&
                        !lower.contains("bin/sh") &&
                        !lower.contains("ps ")
                    }"""

new_is_active_2 = """                    res.stdout.lines().any { line ->
                        isValidOpenVpnProcessLine(line)
                    }"""
content = content.replace(old_is_active_2, new_is_active_2)


old_extract = """        for (line in psOutput.lines()) {
            val lower = line.lowercase()
            val isProcessLine = lower.contains("openvpn") && 
                    !lower.contains("grep") && 
                    !lower.contains("echo") && 
                    !lower.contains("ps |") &&
                    !lower.contains("sh -c") &&
                    !lower.contains("bin/sh") &&
                    !lower.contains("ps ") &&
                    !lower.contains("uci ") &&
                    !lower.contains("export path=")
            
            if (isProcessLine) {"""

new_extract = """        for (line in psOutput.lines()) {
            if (isValidOpenVpnProcessLine(line)) {"""
content = content.replace(old_extract, new_extract)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w", encoding="utf-8") as f:
    f.write(content)
