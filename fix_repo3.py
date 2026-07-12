with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    if "===NLBWMON===" in line and "val cmd =" in line:
        new_lines.append("""        var tempCmd = ""
        val tempSource = config.capabilities.temperatureSource
        val tempPaths = config.capabilities.temperaturePaths
        if (tempSource == "THERMAL_ZONE" || tempSource == "HWMON") {
            val paths = tempPaths.split(",").filter { it.isNotBlank() }.joinToString(" ")
            if (paths.isNotEmpty()) {
                if (tempSource == "THERMAL_ZONE") {
                    tempCmd = "echo '===TEMP==='; for p in $paths; do if [ -f \\$p/temp ]; then cat \\$p/temp; fi; done; "
                } else if (tempSource == "HWMON") {
                    tempCmd = "echo '===TEMP==='; for p in $paths; do if [ -f \\$p ]; then cat \\$p; fi; done; "
                }
            }
        }
""")
        new_lines.append(line.replace("$ethernetCmd\"", "$ethernetCmd$tempCmd\""))
    else:
        new_lines.append(line)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.writelines(new_lines)
