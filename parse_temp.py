with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    if "val cpuUsage = parseCpuUsage(cpuPart)" in line:
        new_lines.append("""            var tempPart = ""
            if (stdout.contains("===TEMP===")) {
                tempPart = stdout.substringAfter("===TEMP===").substringBefore("===").trim()
            }
            var cpuTemperature: Float? = null
            if (tempPart.isNotEmpty()) {
                val tempValues = tempPart.lines().mapNotNull { it.trim().toFloatOrNull() }
                if (tempValues.isNotEmpty()) {
                    val tempsInC = tempValues.map { it / 1000f }.filter { it in -20f..150f }
                    if (tempsInC.isNotEmpty()) {
                        cpuTemperature = tempsInC.maxOrNull()
                    }
                }
            }
""")
    if "TrafficAndTelemetry(" in line and "rxBytes = rx," in lines[lines.index(line) + 1]:
        new_lines.append(line)
        continue
    if "memoryUsage = memoryUsage," in line and "val uptime = parseUptime(uptimePart)" in lines[lines.index(line) - 20:lines.index(line) + 20]:
        new_lines.append("                cpuTemperature = cpuTemperature,\n")
    new_lines.append(line)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.writelines(new_lines)
