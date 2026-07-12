import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

check_cmd = """            echo '===BOARD_JSON==='
            if [ -f /etc/board.json ] && grep -q '"switch"' /etc/board.json 2>/dev/null; then echo 1; else echo 0; fi
            echo '===TEMP_SRC==='
            if ls /sys/class/thermal/ 2>/dev/null | grep thermal_zone >/dev/null 2>&1; then
                echo 'THERMAL_ZONE'
                ls -d /sys/class/thermal/thermal_zone* 2>/dev/null | tr '\\n' ',' | sed 's/,$//'
            elif find /sys/class/hwmon/ -name "temp*_input" 2>/dev/null | grep input >/dev/null 2>&1; then
                echo 'HWMON'
                find /sys/class/hwmon/ -name "temp*_input" 2>/dev/null | tr '\\n' ',' | sed 's/,$//'
            else
                echo 'UNSUPPORTED'
            fi"""

content = content.replace("""            echo '===BOARD_JSON==='
            if [ -f /etc/board.json ] && grep -q '"switch"' /etc/board.json 2>/dev/null; then echo 1; else echo 0; fi""", check_cmd)

extract = """            val boardPart = extractSection(out, "===BOARD_JSON===", "===TEMP_SRC===").trim()
            val tempSrcPart = extractSection(out, "===TEMP_SRC===", null).trim()
            
            var tempSource = "UNSUPPORTED"
            var tempPaths = ""
            if (tempSrcPart.startsWith("THERMAL_ZONE")) {
                tempSource = "THERMAL_ZONE"
                tempPaths = tempSrcPart.removePrefix("THERMAL_ZONE").trim()
            } else if (tempSrcPart.startsWith("HWMON")) {
                tempSource = "HWMON"
                tempPaths = tempSrcPart.removePrefix("HWMON").trim()
            }"""

content = content.replace("""            val boardPart = extractSection(out, "===BOARD_JSON===", null).trim()""", extract)

caps = """            val caps = RouterCapabilities(
                switchArchitecture = arch,
                hasBridgeUtil = bridgePart == "1",
                hasSwconfigUtil = swconfigPart == "1",
                hasBoardJsonWithSwitchSection = boardPart == "1",
                hasMibCounters = config.capabilities.hasMibCounters,
                temperatureSource = tempSource,
                temperaturePaths = tempPaths,
                lastCheckedTimestamp = System.currentTimeMillis()
            )"""

content = re.sub(r'val caps = RouterCapabilities\([^\)]+\)', caps, content)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)
