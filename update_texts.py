import glob
import os
import xml.etree.ElementTree as ET
import urllib.request
import urllib.parse
import json
import time

def translate_text(text, target_lang):
    if not text: return text
    if target_lang == 'ru': return text
    
    url = f"https://translate.googleapis.com/translate_a/single?client=gtx&sl=ru&tl={target_lang}&dt=t&q={urllib.parse.quote(text)}"
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
    try:
        response = urllib.request.urlopen(req)
        data = json.loads(response.read().decode('utf-8'))
        res = "".join([d[0] for d in data[0] if d[0]])
        # Fix quotes
        res = res.replace("'", r"\'")
        return res
    except Exception as e:
        print(f"Error translating '{text}' to {target_lang}: {e}")
        return text

updates = {
    'upload_arrow': 'Отдача ↑',
    'uptime': 'Время работы',
    'tab_main_dashboard': 'Домой',
    'tab_console': 'Консоль',
    'tab_tools': 'Инструменты',
    'tab_reboot': 'Перезапуск'
}

tab_keys = ['tab_main_dashboard', 'tab_console', 'tab_tools', 'tab_reboot']

files = glob.glob('app/src/main/res/values-*/strings.xml') + ['app/src/main/res/values/strings.xml']

for filepath in files:
    if filepath == 'app/src/main/res/values/strings.xml':
        lang = 'en'
    else:
        lang = filepath.split('values-')[1].split('/')[0]
    
    print(f"Updating {lang}...")
    
    tree = ET.parse(filepath)
    root = tree.getroot()
    changed = False
    
    for key, ru_val in updates.items():
        # Find element
        el = None
        for child in root:
            if child.attrib.get('name') == key:
                el = child
                break
        
        if el is not None:
            # We want to translate
            translated = translate_text(ru_val, lang)
            
            # Capitalize if it's a tab key
            if key in tab_keys and translated:
                translated = translated[0].upper() + translated[1:]
                
            if el.text != translated:
                el.text = translated
                changed = True
        else:
            # Create element
            el = ET.Element('string', name=key)
            translated = translate_text(ru_val, lang)
            if key in tab_keys and translated:
                translated = translated[0].upper() + translated[1:]
            el.text = translated
            root.append(el)
            changed = True
            
        time.sleep(0.5) # Sleep to avoid rate limiting
        
    if changed:
        tree.write(filepath, encoding='utf-8', xml_declaration=True)

print("Done updating texts.")
