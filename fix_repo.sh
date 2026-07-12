#!/bin/bash
awk '
/val cmd = "route_iface=\$\(ip route/ {
    print "        var tempCmd = \"\""
    print "        val tempSource = config.capabilities.temperatureSource"
    print "        val tempPaths = config.capabilities.temperaturePaths"
    print "        if (tempSource == \"THERMAL_ZONE\" || tempSource == \"HWMON\") {"
    print "            val paths = tempPaths.split(\",\").filter { it.isNotBlank() }.joinToString(\" \")"
    print "            if (paths.isNotEmpty()) {"
    print "                if (tempSource == \"THERMAL_ZONE\") {"
    print "                    tempCmd = \"echo '\\''===TEMP==='\\''; for p in $paths; do if [ -f \\$p/temp ]; then cat \\$p/temp; fi; done; \""
    print "                } else if (tempSource == \"HWMON\") {"
    print "                    tempCmd = \"echo '\\''===TEMP==='\\''; for p in $paths; do if [ -f \\$p ]; then cat \\$p; fi; done; \""
    print "                }"
    print "            }"
    print "        }"
    sub(/\$ethernetCmd\"/, "$ethernetCmd$tempCmd\"")
}
{ print }
' app/src/main/java/com/example/data/RouterRepository.kt > temp.kt
mv temp.kt app/src/main/java/com/example/data/RouterRepository.kt
