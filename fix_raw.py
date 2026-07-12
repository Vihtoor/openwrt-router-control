with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    repo = f.read()

start_idx = repo.find("suspend fun queryWanTrafficBytes")
end_idx = repo.find("val rxTxPart = extractSection")

new_code = """suspend fun queryWanTrafficBytes(config: RouterConfig): TrafficAndTelemetry {
        val arch = config.capabilities.switchArchitecture
        val hasBridge = config.capabilities.hasBridgeUtil
        val hasSwconfig = config.capabilities.hasSwconfigUtil
        
        var ethernetCmd = ""
        if (hasBridge) {
            ethernetCmd += "echo '===BRIDGE_FDB==='; bridge fdb show br-lan 2>/dev/null | grep dev | grep -v 'self'; "
        }
        if (arch == SwitchArchitecture.DSA) {
            ethernetCmd += "echo '===DSA_PORTS==='; for lan in \\$(ls /sys/class/net/ 2>/dev/null | grep -E '^lan[0-9]+\\$'); do carrier=\\$(cat /sys/class/net/\\$lan/carrier 2>/dev/null || echo 0); speed=\\$(cat /sys/class/net/\\$lan/speed 2>/dev/null || echo 0); rx=\\$(cat /sys/class/net/\\$lan/statistics/rx_bytes 2>/dev/null || echo 0); tx=\\$(cat /sys/class/net/\\$lan/statistics/tx_bytes 2>/dev/null || echo 0); echo \\"\\$lan \\$carrier \\$speed \\$rx \\$tx\\"; done; "
        } else if (arch == SwitchArchitecture.SWCONFIG && hasSwconfig) {
            ethernetCmd += "echo '===SWCONFIG_PORTS==='; swconfig dev switch0 show 2>/dev/null; "
        }

        if (arch == SwitchArchitecture.SWCONFIG && config.capabilities.hasBoardJsonWithSwitchSection) {
            ethernetCmd += "echo '===BOARD_JSON_SWITCH==='; cat /etc/board.json 2>/dev/null; "
        }

        val cmd = "route_iface=\\$(ip route | grep '^default' | head -n1 | awk '{print \\$5}'); if [ -n \\"\\$route_iface\\" ] && [ -d \\"/sys/class/net/\\$route_iface\\" ]; then cat /sys/class/net/\\$route_iface/statistics/rx_bytes; cat /sys/class/net/\\$route_iface/statistics/tx_bytes; else cat /proc/net/dev | grep -E \\"(eth|wan|dsl|wlan|rmnet|ppp)\\" | head -n1 | tr ':' ' ' | awk '{print \\$2; print \\$10}'; fi; echo '===CPU==='; top_out=\\$(top -b -n 1 | grep -i 'cpu' | head -n 1); if [ -n \\"\\$top_out\\" ]; then echo \\"\\$top_out\\"; else cat /proc/loadavg 2>/dev/null; fi; echo '===MEM==='; cat /proc/meminfo 2>/dev/null | grep -E '^(MemTotal|MemFree|MemAvailable|Buffers|Cached):'; echo '===UPTIME==='; cat /proc/uptime 2>/dev/null; echo '===WIFI==='; for iface in \\$(iw dev 2>/dev/null | grep Interface | awk '{print \\$2}'); do freq=\\$(iw dev \\$iface info 2>/dev/null | grep -oE '[0-9]+ MHz' | head -n1 | awk '{print \\$1}'); [ -z \\"\\$freq\\" ] && freq=0; iw dev \\$iface station dump | awk -v ifc=\\"\\$iface\\" -v f=\\"\\$freq\\" '/^Station/ { mac=\\$2; rxb=\\"\\"; txb=\\"\\" } /rx bytes:/ { rx=\\$3 } /tx bytes:/ { tx=\\$3 } /rx bitrate:/ { rxb=\\$3\\" \\"\\$4 } /tx bitrate:/ { txb=\\$3\\" \\"\\$4; print mac, rx, tx, ifc, f, rxb, txb }'; done; echo '===LEASES==='; cat /tmp/dhcp.leases 2>/dev/null; echo '===ARP==='; cat /proc/net/arp 2>/dev/null; echo '===NLBWMON==='; if command -v nlbw >/dev/null; then nlbw -c csv -g mac 2>/dev/null; fi; $ethernetCmd"
        return try {
            val res = sshClientManager.executeCommand(config, cmd)
            val stdout = res.stdout

            """

repo = repo[:start_idx] + new_code + repo[end_idx:]

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(repo)
