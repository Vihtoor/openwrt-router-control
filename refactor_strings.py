import re
import os
import xml.etree.ElementTree as ET
import urllib.request
import urllib.parse
import json

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

def translate_to_en(text):
    if not text: return text
    url = f"https://translate.googleapis.com/translate_a/single?client=gtx&sl=ru&tl=en&dt=t&q={urllib.parse.quote(text)}"
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
    try:
        response = urllib.request.urlopen(req)
        data = json.loads(response.read().decode('utf-8'))
        return "".join([d[0] for d in data[0] if d[0]])
    except Exception as e:
        print(f"Error translating '{text}' to en: {e}")
        return text

# 1. Update MainActivity.kt
with open('app/src/main/java/com/example/MainActivity.kt', 'r', encoding='utf-8') as f:
    content = f.read()

for ru_text, key in mapping.items():
    # Replace translateText("ru_text", context) -> context.getString(R.string.key)
    # Be careful with the context variable name, sometimes it's `androidx.compose.ui.platform.LocalContext.current`
    
    # We will use regex to find translateText("ru_text", <context_var>)
    pattern_translate = r'translateText\(\s*"' + re.escape(ru_text) + r'"\s*,\s*([^)]+)\)'
    content = re.sub(pattern_translate, rf'\1.getString(R.string.{key})', content)
    
    # Replace bare strings
    # We only want to replace bare strings in specific assignments
    # text = "ru_text" -> text = stringResource(R.string.key)
    content = re.sub(r'text\s*=\s*"' + re.escape(ru_text) + r'"', rf'text = stringResource(R.string.{key})', content)
    # Text("ru_text") -> Text(stringResource(R.string.key))
    content = re.sub(r'Text\(\s*"' + re.escape(ru_text) + r'"', rf'Text(stringResource(R.string.{key})', content)
    # contentDescription = "ru_text" -> contentDescription = stringResource(R.string.key)
    content = re.sub(r'contentDescription\s*=\s*"' + re.escape(ru_text) + r'"', rf'contentDescription = stringResource(R.string.{key})', content)
    # label = "ru_text" -> label = stringResource(R.string.key)
    content = re.sub(r'label\s*=\s*"' + re.escape(ru_text) + r'"', rf'label = stringResource(R.string.{key})', content)
    # title = "ru_text" -> title = stringResource(R.string.key)
    content = re.sub(r'title\s*=\s*"' + re.escape(ru_text) + r'"', rf'title = stringResource(R.string.{key})', content)

with open('app/src/main/java/com/example/MainActivity.kt', 'w', encoding='utf-8') as f:
    f.write(content)

# 2. Add to strings.xml (English) and values-ru/strings.xml (Russian)
def add_to_xml(filepath, key, text):
    tree = ET.parse(filepath)
    root = tree.getroot()
    # Check if exists
    for child in root:
        if child.attrib.get('name') == key:
            child.text = text
            tree.write(filepath, encoding='utf-8', xml_declaration=True)
            return
    el = ET.Element('string', name=key)
    el.text = text
    root.append(el)
    tree.write(filepath, encoding='utf-8', xml_declaration=True)

# Generate English translations for base
print("Translating keys to English for base strings.xml...")
en_mapping = {}
for ru_text, key in mapping.items():
    en_mapping[key] = translate_to_en(ru_text)

base_file = 'app/src/main/res/values/strings.xml'
ru_file = 'app/src/main/res/values-ru/strings.xml'

for ru_text, key in mapping.items():
    add_to_xml(base_file, key, en_mapping[key])
    add_to_xml(ru_file, key, ru_text)

print("Done refactoring Kotlin and adding to XML.")
