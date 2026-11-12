import re

mapping = {
    'IP адрес DNS сервера': 'dns_ip_address',
    'SSH терминал': 'ssh_terminal',
    'Активен': 'status_active',
    'Аптайм': 'uptime',
    'Все VPN отключены': 'all_vpn_disabled',
    'Вы уверены, что хотите перезагрузить роутер? Это временно прервет сетевое соединение.': 'dialog_reboot_msg',
    'Выберите DNS для использования на роутере.': 'msg_select_dns',
    'Выбор DNS': 'dns_selection',
    'Выгрузка ↑': 'upload_arrow',
    'Главная': 'tab_main_dashboard',
    'Добавить': 'action_add',
    'Добавить в избранное': 'action_add_favorite',
    'Добавьте один или несколько IP, разделяя их запятыми': 'msg_add_ips_comma',
    'Дополнительный DNS': 'additional_dns',
    'Доступные VPN. Выберите те, которые должны быть включены': 'msg_available_vpn_desc',
    'Желаете ли вы добавить дополнительный DNS?': 'msg_want_additional_dns',
    'Желаете ли вы добавить один из публичных DNS?': 'msg_want_public_dns',
    'Загрузка ↓': 'download_arrow',
    'Задать': 'action_set',
    'Инструменты': 'tab_tools',
    'Команда': 'label_command',
    'Консоль': 'tab_console',
    'Копировать': 'action_copy',
    'Локация': 'label_location',
    'Назад': 'action_back',
    'Настройки': 'action_settings',
    'Нет': 'action_no',
    'Нет активных VPN соединений': 'msg_no_active_vpn',
    'ОЗУ': 'label_ram',
    'Обновить статус': 'action_refresh_status',
    'Ожидание данных трафика...': 'msg_waiting_traffic',
    'Отправить': 'action_send',
    'Очистить консоль': 'action_clear_console',
    'Очистить логи': 'action_clear_logs',
    'Перезагрузить': 'action_reboot',
    'Перезагрузка': 'tab_reboot',
    'Перезагрузка роутера': 'dialog_reboot_title',
    'Перезапуск': 'action_restart',
    'По умолчанию (Провайдер)': 'dns_default_isp',
    'Подтвердить': 'action_confirm',
    'Подтверждение DNS': 'dialog_dns_confirm_title',
    'Получение данных от роутера...': 'msg_getting_data_router',
    'Получение данных...': 'msg_getting_data',
    'Пользовательский DNS': 'dns_custom',
    'Предыдущая команда': 'action_prev_command',
    'Применение изменений...': 'msg_applying_changes',
    'Применить': 'action_apply',
    'Провайдер': 'label_isp',
    'Процессор': 'label_cpu',
    'Публичный IP': 'label_public_ip',
    'Список соединений': 'label_connections_list',
    'Текст скопирован в буфер обмена': 'msg_text_copied',
    'Температура': 'label_temperature',
    'Установить %s в качестве DNS сервера?': 'msg_set_dns_confirm',
    'ЦП': 'label_cpu_short',
    'дополнительно': 'label_additional',
    'недоступна': 'msg_unavailable'
}

with open('app/src/main/java/com/example/ui/SettingsPanel.kt', 'r', encoding='utf-8') as f:
    content = f.read()

for ru_text, key in mapping.items():
    pattern_translate = r'translateText\(\s*"' + re.escape(ru_text) + r'"\s*,\s*([^)]+)\)'
    content = re.sub(pattern_translate, rf'\1.getString(R.string.{key})', content)
    
    content = re.sub(r'text\s*=\s*"' + re.escape(ru_text) + r'"', rf'text = stringResource(R.string.{key})', content)
    content = re.sub(r'Text\(\s*"' + re.escape(ru_text) + r'"', rf'Text(stringResource(R.string.{key})', content)
    content = re.sub(r'contentDescription\s*=\s*"' + re.escape(ru_text) + r'"', rf'contentDescription = stringResource(R.string.{key})', content)
    content = re.sub(r'label\s*=\s*"' + re.escape(ru_text) + r'"', rf'label = stringResource(R.string.{key})', content)
    content = re.sub(r'title\s*=\s*"' + re.escape(ru_text) + r'"', rf'title = stringResource(R.string.{key})', content)

with open('app/src/main/java/com/example/ui/SettingsPanel.kt', 'w', encoding='utf-8') as f:
    f.write(content)

