import urllib.request
import urllib.parse
import json
import os
import time
import xml.etree.ElementTree as ET

rus_strings = {
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

languages = ["ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv", "en"]

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
    folder = "values" if lang == "en" else f"values-{lang}"
    filepath = f"app/src/main/res/{folder}/strings.xml"
    if not os.path.exists(filepath):
        continue
        
    tree = ET.parse(filepath)
    root = tree.getroot()
    existing_keys = set([child.attrib.get('name') for child in root if child.tag == 'string'])
    
    modified = False
    for key, ru_text in rus_strings.items():
        if key not in existing_keys:
            translated = translate(ru_text, lang)
            translated = translated.replace("％", "%").replace("% d", "%d").replace("% .1f", "%.1f").replace("%. 1f", "%.1f").replace(" .", ".")
            translated = translated.replace("'%s'", r"\'%s\'").replace("'", r"\'")
            new_elem = ET.Element("string", {"name": key})
            new_elem.text = translated
            root.append(new_elem)
            time.sleep(0.5)
            modified = True
            
    if modified:
        ET.indent(tree, space="    ", level=0)
        tree.write(filepath, encoding='utf-8', xml_declaration=True)
