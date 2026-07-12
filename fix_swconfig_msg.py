import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    main = f.read()

old_logic = """    if (caps.switchArchitecture == com.example.data.SwitchArchitecture.SWCONFIG) {
        if (!caps.hasSwconfigUtil) {
            messages.add("Не найдена утилита swconfig — недоступен линк-статус и трафик LAN-портов на этом типе роутера.")
            actions.add("Установите пакет swconfig через opkg")
        } else if (!caps.hasBoardJsonWithSwitchSection) {
            messages.add("Невозможно сопоставить логические порты с физическими номерами LAN-портов.")
        }
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNSUPPORTED) {
        messages.add("Роутер/прошивка не поддерживает разделение LAN-портов (например, простой роутер без управляемого свича, где все LAN-порты объединены в один eth0).")
    }"""

new_logic = """    if (!caps.hasSwconfigUtil && caps.switchArchitecture != com.example.data.SwitchArchitecture.DSA) {
        messages.add("Не найдена утилита swconfig — недоступен линк-статус и трафик LAN-портов (если роутер поддерживает swconfig).")
        actions.add("Установите пакет swconfig через opkg")
    }
    
    if (caps.switchArchitecture == com.example.data.SwitchArchitecture.SWCONFIG) {
        if (!caps.hasBoardJsonWithSwitchSection) {
            messages.add("Невозможно сопоставить логические порты с физическими номерами LAN-портов.")
        }
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNSUPPORTED) {
        messages.add("Роутер/прошивка не поддерживает разделение LAN-портов (архитектура: ${caps.switchArchitecture}).")
    }"""

main = main.replace(old_logic, new_logic)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(main)
