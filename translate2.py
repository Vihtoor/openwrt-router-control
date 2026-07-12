import urllib.request
import urllib.parse
import json
import os
import time
import xml.etree.ElementTree as ET

rus_strings = {
    "no_ethernet_devices": "Устройств подключенных к Ethernet портам не обнаружено",
    "wifi_6_ghz": "Wi-Fi 6 ГГц",
    "wifi_5_ghz": "Wi-Fi 5 ГГц",
    "wifi_24_ghz": "Wi-Fi 2.4 ГГц",
    "wifi_other": "Wi-Fi (Другое)",
    "ethernet": "Ethernet",
    "dns_desc_cloudflare": "Один из самых быстрых публичных DNS-провайдеров в мире с акцентом на конфиденциальность.",
    "dns_cloudflare_default": "Базовый (быстрый доступ и приватность)",
    "dns_cloudflare_malware": "Защита от вредоносного ПО (Malware Block)",
    "dns_cloudflare_family": "Семейный (блокировка вредоносного ПО + контента для взрослых + SafeSearch)",
    "dns_desc_opendns": "Один из старейших и надежнейших сервисов, принадлежащий компании Cisco.",
    "dns_opendns_home": "Базовый (Home / без фильтрации)",
    "dns_opendns_family": "Семейный (Family Shield / предустановленная фильтрация)",
    "dns_opendns_family_desc": "Подходит для роутеров, фильтрует контент для взрослых без необходимости создания учетной записи.",
    "dns_desc_cleanbrowsing": "Специализированный сервис, ориентированный исключительно на блокировку нежелательного контента.",
    "dns_cleanbrowsing_security": "Фильтр безопасности (Security Filter)",
    "dns_cleanbrowsing_adult": "Взрослый фильтр (Adult Filter)",
    "dns_cleanbrowsing_family": "Семейный фильтр (Family Filter)",
    "dns_cleanbrowsing_family_desc": "Блокирует прокси, VPN-туннели, порнографию и жестко включает безопасный поиск в Google/YouTube.",
    "dns_desc_adguard": "Лучший выбор для тех, кто хочет избавиться от рекламы на уровне сетевых запросов.",
    "dns_adguard_default": "Базовый (блокировка рекламы, трекеров и фишинга)",
    "dns_adguard_family": "Семейный (базовый + контент для взрослых + SafeSearch)",
    "dns_adguard_unfiltered": "Нефильтрующий (просто быстрый и надежный сервис)",
    "dns_desc_controld": "Современный и очень гибкий DNS от создателей Windscribe.",
    "dns_controld_uncensored": "Нефильтрующий (чистый интернет / Uncensored)",
    "dns_controld_ads": "Блокировка рекламы (Ads & Trackers)",
    "dns_controld_family": "Семейный (все: реклама + соцсети + контент для взрослых)",
    "dns_desc_quad9": "Сервис с акцентом на кибербезопасность.",
    "dns_quad9_default": "Стандартный (по умолчанию / блокировка фишинга и малвари)",
    "dns_quad9_ecs": "Стандартный + ECS-поддержка (с поддержкой ECS)",
    "dns_quad9_ecs_desc": "Передает малую часть вашего IP-адреса (EDNS Client Subnet) для более точной маршрутизации до CDN-серверов.",
    "dns_desc_google": "Один из самых популярных публичных сервисов.",
    "dns_google_default": "Стандартный",
    "port_approximate": "(предположительно)",
    "port_unknown": "Подключено по Ethernet, порт не определён",
    "status_restarting": "Перезапуск...",
    "status_waiting_conn": "Ожидание соединения...",
    "status_please_wait": "Пожалуйста, подождите...",
    "ssh_connected": "SSH подключен",
    "ssh_terminal_active": "Терминал активен",
    "ssh_disconnect": "Отключить",
    "format_mb_percent": "%.1f МБ (%.0f%%)",
    "format_days": "%dд ",
    "format_hours": "%dч ",
    "format_minutes": "%dм",
    "label_today_colon": "За сегодня: ",
    "label_ip_colon": "IP: ",
    "label_mac_colon": "MAC: ",
    "msg_press_device_graph": "Нажмите на устройство, чтобы открыть график",
    "msg_no_devices": "Нет подключенных устройств или данные недоступны.",
    "msg_devices_connected": "Подключено %d устройств",
    "msg_no_ethernet": "Устройств подключенных к Ethernet портам не обнаружено",
    "format_speed_dl": "↓ %.2f Мбит/с",
    "format_speed_ul": "↑ %.2f Мбит/с",
    "format_bytes_kb": "%.1f КБ",
    "format_bytes_mb": "%.1f МБ",
    "format_bytes_gb": "%.1f ГБ",
    "msg_link_speed": "Скорость линка: ↓ %s / ↑ %s",
    "msg_shared_traffic_warning": "Внимание: для этого типа подключения показан общий трафик всего физического порта роутера, а не только этого устройства.",
    "msg_traffic_unavailable": "Трафик недоступен на этом роутере",
    "msg_install_nlbwmon": "Установите утилиту nlbwmon для сбора статистики:",
    "format_download_speed": "Загрузка: %.2f Мбит/с",
    "format_upload_speed": "Выгрузка: %.2f Мбит/с",
    "msg_no_selected_device": "Нет выбранного устройства",
    "action_close": "Закрыть"
}

languages = ["lt", "lv", "sv", "en"]

def translate(text, target_lang):
    if target_lang == "ru":
        return text
    url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=ru&tl=" + target_lang + "&dt=t&q=" + urllib.parse.quote(text)
    try:
        req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
        with urllib.request.urlopen(req) as response:
            data = json.loads(response.read().decode())
            return "".join([x[0] for x in data[0]])
    except Exception as e:
        print(f"Error translating to {target_lang}: {e}")
        return text

for lang in languages:
    print(f"Processing {lang}...")
    folder = "values" if lang == "en" else f"values-{lang}"
    filepath = f"app/src/main/res/{folder}/strings.xml"
    
    if not os.path.exists(filepath):
        print(f"Skipping {lang} as {filepath} doesn't exist.")
        continue
        
    tree = ET.parse(filepath)
    root = tree.getroot()
    
    existing_keys = set([child.attrib.get('name') for child in root if child.tag == 'string'])
    
    for key, ru_text in rus_strings.items():
        if key not in existing_keys:
            translated = translate(ru_text, lang)
            # Fix up formatting tokens
            translated = translated.replace("％", "%").replace("% d", "%d").replace("% .1f", "%.1f").replace("%. 1f", "%.1f").replace(" .", ".")
            translated = translated.replace("'%s'", r"\'%s\'").replace("'", r"\'")
            
            new_elem = ET.Element("string", {"name": key})
            new_elem.text = translated
            root.append(new_elem)
            time.sleep(0.5) # Increase sleep to avoid rate limiting
            
    # Pretty print hack
    ET.indent(tree, space="    ", level=0)
    tree.write(filepath, encoding='utf-8', xml_declaration=True)
    print(f"Done inserting strings for {lang}.")
