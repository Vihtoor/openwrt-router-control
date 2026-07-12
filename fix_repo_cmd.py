import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

old_cmd_part = "echo '===WIFI==='; for iface in \\$(iw dev 2>/dev/null | grep Interface | awk '{print \\$2}'); do iw dev \\$iface station dump | awk -v ifc=\\\"\\$iface\\\" '/^Station/ { mac=\\$2 } /rx bytes:/ { rx=\\$3 } /tx bytes:/ { tx=\\$3; print mac, rx, tx, ifc }'; done;"

new_cmd_part = "echo '===WIFI==='; for iface in \\$(iw dev 2>/dev/null | grep Interface | awk '{print \\$2}'); do freq=\\$(iw dev \\$iface info 2>/dev/null | grep -oE '[0-9]+ MHz' | head -n1 | awk '{print \\$1}'); [ -z \\\"\\$freq\\\" ] && freq=0; iw dev \\$iface station dump | awk -v ifc=\\\"\\$iface\\\" -v f=\\\"\\$freq\\\" '/^Station/ { mac=\\$2 } /rx bytes:/ { rx=\\$3 } /tx bytes:/ { tx=\\$3; print mac, rx, tx, ifc, f }'; done;"

content = content.replace(old_cmd_part, new_cmd_part)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)

