import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

replacement = """        var tempCmd = ""
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

        val cmd = "route_iface=\\$(ip route | grep '^default' | head -n1 | awk '{print \\$5}'); if [ -n \\"\\$route_iface\\" ] && [ -d \\"/sys/class/net/\\$route_iface\\" ]; then cat /sys/class/net/\\$route_iface/statistics/rx_bytes; cat /sys/class/net/\\$route_iface/statistics/tx_bytes; else cat /proc/net/dev | grep -E \\"(eth|wan|dsl|wlan|rmnet|ppp)\\" | head -n1 | tr ':' ' ' | awk '{print \\$2; print \\$10}'; fi; echo '===CPU==='; top_out=\\$(top -b -n 1 | grep -i 'cpu' | head -n 1); if [ -n \\"\\$top_out\\" ]; then echo \\"\\$top_out\\"; else cat /proc/loadavg 2>/dev/null; fi; echo '===MEM==='; cat /proc/meminfo 2>/dev/null | grep -E '^(MemTotal|MemFree|MemAvailable|Buffers|Cached):'; echo '===UPTIME==='; cat /proc/uptime 2>/dev/null; echo '===WIFI==='; for iface in \\$(iw dev 2>/dev/null | grep Interface | awk '{print \\$2}'); do freq=\\$(iw dev \\$iface info 2>/dev/null | grep -oE '[0-9]+ MHz' | head -n1 | awk '{print \\$1}'); [ -z \\"\\$freq\\" ] && freq=0; iw dev \\$iface station dump | awk -v ifc=\\"\\$iface\\" -v f=\\"\\$freq\\" 'BEGIN{mac=\\"\\"} /^Station/ { if(mac!=\\"\\") { if(rxb==\\"\\") rxb=\\"-\\"; if(txb==\\"\\") txb=\\"-\\"; print mac, rx, tx, ifc, f, rxb, txb } mac=\\$2; rx=0; tx=0; rxb=\\"\\"; txb=\\"\\" } /rx bytes:/ { rx=\\$3 } /tx bytes:/ { tx=\\$3 } /rx bitrate:/ { rxb=\\$3\\" \\"\\$4 } /tx bitrate:/ { txb=\\$3\\" \\"\\$4 } END { if(mac!=\\"\\") { if(rxb==\\"\\") rxb=\\"-\\"; if(txb==\\"\\") txb=\\"-\\"; print mac, rx, tx, ifc, f, rxb, txb } }'; done; echo '===LEASES==='; cat /tmp/dhcp.leases 2>/dev/null; echo '===ARP==='; cat /proc/net/arp 2>/dev/null; echo '===NLBWMON==='; if command -v nlbw >/dev/null; then nlbw -c csv -g mac 2>/dev/null; fi; \\$ethernetCmd\\$tempCmd\""""

content = re.sub(r'val cmd = "route_iface=\$\(ip route(.*?)"', replacement.replace('\\', '\\\\'), content, flags=re.DOTALL)
with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)
