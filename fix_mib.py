with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    repo = f.read()

old_code = """        if (config.capabilities.switchArchitecture == SwitchArchitecture.DSA) {
                    if (dsaData.containsKey(dev)) {
                        connectionType = "LAN (\$dev)"
                        devRx = dsaData[dev]!![0]
                        devTx = dsaData[dev]!![1]
                    } else {
                        connectionType = "Ethernet"
                    }
                } else if (config.capabilities.switchArchitecture == SwitchArchitecture.SWCONFIG) {
                    connectionType = if (dev.startsWith("eth0")) "LAN (\$eth0PhysicalPorts)" else "LAN (\$dev)"
                    // Since multiple MACs share eth0, we cannot map rx/tx from swconfig per device accurately,
                    // but we can pass 0 for now and let the UI know traffic is unavailable or use nlbwmon.
                    // Wait, the prompt says for SWCONFIG: 
                    // "вычислять как дельту между двумя опросами, делённую на интервал времени (учесть что здесь именно накопительные байтовые счётчики, а не мгновенный bitrate как в Wi-Fi)"
                    // But if swconfig gives stats per PORT and we can't map MAC to port, we can't show it per MAC.
                    // The prompt: "Ветка SWCONFIG: ... Линк-статус и MIB-счётчики (если поддерживаются чипом): swconfig dev switch0 show ... Если их нет, трафик помечать как нет данных".
                    // Actually, if we just give 0 for rx/tx, we'll use nlbw Rx/Tx as month bytes.
                    devRx = 0L
                    devTx = 0L
                }"""

new_code = """        if (config.capabilities.switchArchitecture == SwitchArchitecture.DSA) {
                    if (dsaData.containsKey(dev)) {
                        connectionType = "LAN (\$dev)"
                        devRx = dsaData[dev]!![0]
                        devTx = dsaData[dev]!![1]
                    } else {
                        connectionType = "Ethernet"
                    }
                } else if (config.capabilities.switchArchitecture == SwitchArchitecture.SWCONFIG) {
                    connectionType = if (dev.startsWith("eth0")) "LAN (\$eth0PhysicalPorts)" else "LAN (\$dev)"
                    devRx = -1L // Signal unavailable traffic
                    devTx = -1L
                }"""

repo = repo.replace(old_code, new_code)
with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(repo)
