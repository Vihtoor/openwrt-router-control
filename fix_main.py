with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace('"Wi-Fi 6 ГГц"', 'context.getString(R.string.wifi_6_ghz)')
content = content.replace('"Wi-Fi 5 ГГц"', 'context.getString(R.string.wifi_5_ghz)')
content = content.replace('"Wi-Fi 2.4 ГГц"', 'context.getString(R.string.wifi_24_ghz)')
content = content.replace('"Wi-Fi (Другое)"', 'context.getString(R.string.wifi_other)')
content = content.replace('"Ethernet"', 'context.getString(R.string.ethernet)')

content = content.replace('"Выберите устройство, чтобы открыть график"', 'stringResource(R.string.msg_press_device_graph)')
content = content.replace('"Нет подключенных устройств или данные недоступны."', 'stringResource(R.string.msg_no_devices)')
content = content.replace('"Подключено ${devicesInGroup.size} устройств"', 'String.format(stringResource(R.string.msg_devices_connected), devicesInGroup.size)')
content = content.replace('"Устройств подключенных к Ethernet портам не обнаружено"', 'stringResource(R.string.msg_no_ethernet)')

content = content.replace('"↓ ${String.format(java.util.Locale.US, "%.2f", device.downloadSpeedMbps)} Мбит/с"', 'String.format(stringResource(R.string.format_speed_dl), device.downloadSpeedMbps)')
content = content.replace('"↑ ${String.format(java.util.Locale.US, "%.2f", device.uploadSpeedMbps)} Мбит/с"', 'String.format(stringResource(R.string.format_speed_ul), device.uploadSpeedMbps)')

content = content.replace('String.format(java.util.Locale.US, "%.1f КБ", totalBytes / 1024f)', 'String.format(stringResource(R.string.format_bytes_kb), totalBytes / 1024f)')
content = content.replace('String.format(java.util.Locale.US, "%.1f МБ", totalBytes / (1024f * 1024f))', 'String.format(stringResource(R.string.format_bytes_mb), totalBytes / (1024f * 1024f))')
content = content.replace('String.format(java.util.Locale.US, "%.1f ГБ", totalBytes / (1024f * 1024f * 1024f))', 'String.format(stringResource(R.string.format_bytes_gb), totalBytes / (1024f * 1024f * 1024f))')

content = content.replace('String.format("%.1f КБ", bytes / 1024f)', 'String.format(stringResource(R.string.format_bytes_kb), bytes / 1024f)')
content = content.replace('String.format("%.1f МБ", bytes / (1024f * 1024f))', 'String.format(stringResource(R.string.format_bytes_mb), bytes / (1024f * 1024f))')
content = content.replace('String.format("%.1f ГБ", bytes / (1024f * 1024f * 1024f))', 'String.format(stringResource(R.string.format_bytes_gb), bytes / (1024f * 1024f * 1024f))')

content = content.replace('"Скорость линка: ↓ ${rightPaneDevice.wifiRxBitrate} / ↑ ${rightPaneDevice.wifiTxBitrate}"', 'String.format(stringResource(R.string.msg_link_speed), rightPaneDevice.wifiRxBitrate ?: "—", rightPaneDevice.wifiTxBitrate ?: "—")')
content = content.replace('"Скорость линка: ↓ ${currentDevice.wifiRxBitrate} / ↑ ${currentDevice.wifiTxBitrate}"', 'String.format(stringResource(R.string.msg_link_speed), currentDevice.wifiRxBitrate ?: "—", currentDevice.wifiTxBitrate ?: "—")')

content = content.replace('"Внимание: для этого типа подключения показан общий трафик всего физического порта роутера, а не только этого устройства."', 'stringResource(R.string.msg_shared_traffic_warning)')
content = content.replace('"Трафик недоступен на этом роутере"', 'stringResource(R.string.msg_traffic_unavailable)')
content = content.replace('"Установите утилиту nlbwmon для сбора статистики:"', 'stringResource(R.string.msg_install_nlbwmon)')

content = content.replace('String.format("Загрузка: %.2f Мбит/с", rightPaneDevice.downloadSpeedMbps)', 'String.format(stringResource(R.string.format_download_speed), rightPaneDevice.downloadSpeedMbps)')
content = content.replace('String.format("Выгрузка: %.2f Мбит/с", rightPaneDevice.uploadSpeedMbps)', 'String.format(stringResource(R.string.format_upload_speed), rightPaneDevice.uploadSpeedMbps)')
content = content.replace('String.format("Загрузка: %.2f Мбит/с", currentDevice.downloadSpeedMbps)', 'String.format(stringResource(R.string.format_download_speed), currentDevice.downloadSpeedMbps)')
content = content.replace('String.format("Выгрузка: %.2f Мбит/с", currentDevice.uploadSpeedMbps)', 'String.format(stringResource(R.string.format_upload_speed), currentDevice.uploadSpeedMbps)')

content = content.replace('"За сегодня: "', 'stringResource(R.string.label_today_colon)')
content = content.replace('"Нет выбранного устройства"', 'stringResource(R.string.msg_no_selected_device)')
content = content.replace('"Нажмите на устройство, чтобы открыть график"', 'stringResource(R.string.msg_press_device_graph)')
content = content.replace('"Закрыть"', 'stringResource(R.string.action_close)')

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
