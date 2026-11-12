import glob
import xml.etree.ElementTree as ET
import urllib.request
import urllib.parse
import json
import time

def translate_text(text, target_lang):
    if target_lang == 'ru': return text
    if target_lang == 'en':
        if text == "Светодиод роутера": return "Router LED"
        if text == "Включен": return "On"
    
    url = f"https://translate.googleapis.com/translate_a/single?client=gtx&sl=ru&tl={target_lang}&dt=t&q={urllib.parse.quote(text)}"
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
    try:
        response = urllib.request.urlopen(req)
        data = json.loads(response.read().decode('utf-8'))
        res = "".join([d[0] for d in data[0] if d[0]])
        res = res.replace("'", r"\'")
        return res
    except Exception as e:
        print(f"Error translating '{text}' to {target_lang}: {e}")
        return text

updates = {
    'title_router_led': 'Светодиод роутера',
    'status_on': 'Включен'
}

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
        el = None
        for child in root:
            if child.attrib.get('name') == key:
                el = child
                break
        
        if el is None:
            el = ET.Element('string', name=key)
            translated = translate_text(ru_val, lang)
            el.text = translated
            root.append(el)
            changed = True
            time.sleep(0.5)
            
    if changed:
        tree.write(filepath, encoding='utf-8', xml_declaration=True)

print("Done updating texts.")
