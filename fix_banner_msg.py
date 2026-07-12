import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    main = f.read()

old_banner = """    var message = ""
    var action = ""
    
    if (!caps.hasBridgeUtil) {
        message = "Не найдена утилита bridge — определение устройств на LAN-портах недоступно."
        action = "Установите пакет ip-bridge через opkg"
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.SWCONFIG) {
        if (!caps.hasSwconfigUtil) {
            message = "Не найдена утилита swconfig — недоступен линк-статус и трафик LAN-портов на этом типе роутера."
            action = "Установите пакет swconfig через opkg"
        } else if (!caps.hasBoardJsonWithSwitchSection) {
            message = "Невозможно сопоставить логические порты с физическими номерами LAN-портов."
        }
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNSUPPORTED) {
        message = "Роутер/прошивка не поддерживает разделение LAN-портов (например, простой роутер без управляемого свича, где все LAN-порты объединены в один eth0)."
    }"""

new_banner = """    val messages = mutableListOf<String>()
    val actions = mutableListOf<String>()
    
    if (!caps.hasBridgeUtil) {
        messages.add("Не найдена утилита bridge — определение устройств на LAN-портах недоступно.")
        actions.add("Установите пакет ip-bridge через opkg")
    }
    if (caps.switchArchitecture == com.example.data.SwitchArchitecture.SWCONFIG) {
        if (!caps.hasSwconfigUtil) {
            messages.add("Не найдена утилита swconfig — недоступен линк-статус и трафик LAN-портов на этом типе роутера.")
            actions.add("Установите пакет swconfig через opkg")
        } else if (!caps.hasBoardJsonWithSwitchSection) {
            messages.add("Невозможно сопоставить логические порты с физическими номерами LAN-портов.")
        }
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNSUPPORTED) {
        messages.add("Роутер/прошивка не поддерживает разделение LAN-портов (например, простой роутер без управляемого свича, где все LAN-порты объединены в один eth0).")
    }
    
    val message = messages.joinToString("\\n\\n")
    val action = actions.joinToString("\\n")"""

main = main.replace(old_banner, new_banner)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(main)
