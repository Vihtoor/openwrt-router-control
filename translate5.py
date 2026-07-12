import urllib.request
import urllib.parse
import json
import os
import time
import xml.etree.ElementTree as ET

rus_strings = {
    "format_speed_mbps": "%.2f Мбит/с",
    "format_temp_celsius": "%.1f °C"
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
            translated = translated.replace("％", "%").replace("% .2f", "%.2f").replace("%. 2f", "%.2f").replace("% .1f", "%.1f").replace("%. 1f", "%.1f").replace(" .", ".")
            translated = translated.replace("'%s'", r"\'%s\'").replace("'", r"\'")
            new_elem = ET.Element("string", {"name": key})
            new_elem.text = translated
            root.append(new_elem)
            time.sleep(0.5)
            modified = True
            
    if modified:
        ET.indent(tree, space="    ", level=0)
        tree.write(filepath, encoding='utf-8', xml_declaration=True)
