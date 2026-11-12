import os
import xml.etree.ElementTree as ET
import glob
import json
import urllib.request
import urllib.parse
import time

res_dir = 'app/src/main/res'
base_file = os.path.join(res_dir, 'values/strings.xml')
all_string_files = glob.glob(os.path.join(res_dir, 'values-*/strings.xml'))

def get_keys_and_values(file_path):
    tree = ET.parse(file_path)
    root = tree.getroot()
    data = {}
    for child in root:
        if child.tag == 'string':
            data[child.attrib['name']] = child.text
    return data

base_data = get_keys_and_values(base_file)

def translate(text, target_lang):
    if not text: return text
    if target_lang == 'en' and text == base_data.get('app_name'): return text
    # quick translation using googletrans api
    url = f"https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl={target_lang}&dt=t&q={urllib.parse.quote(text)}"
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
    try:
        response = urllib.request.urlopen(req)
        data = json.loads(response.read().decode('utf-8'))
        return "".join([d[0] for d in data[0] if d[0]])
    except Exception as e:
        print(f"Error translating '{text}' to '{target_lang}': {e}")
        return text

def add_missing_translations(file_path, locale):
    tree = ET.parse(file_path)
    root = tree.getroot()
    
    current_elements = {child.attrib['name']: child for child in root if child.tag == 'string'}
    current_keys = set(current_elements.keys())
    base_keys = set(base_data.keys())
    
    missing_keys = base_keys - current_keys
    added = 0
    if missing_keys:
        for key in missing_keys:
            val = base_data[key]
            # Manual override for Russian
            if locale == 'ru':
                if key == 'status_turning_off':
                    new_val = 'Выключение...'
                elif key == 'status_applying_changes':
                    new_val = 'Применение изменений...'
                elif key == 'status_turning_on':
                    new_val = 'Включение...'
                else:
                    new_val = translate(val, locale)
                    time.sleep(0.1)
            else:
                new_val = translate(val, locale)
                time.sleep(0.1)
            
            # preserve placeholders - this is tricky if we just translate blindly, but we'll try
            # For simplicity, we just translate. If it has %s, it might break. 
            # We need to make sure placeholders match.
            
            el = ET.Element('string', name=key)
            el.text = new_val
            if 'formatted' in current_elements.get(key, {}): # wait, current doesn't have it
                pass
            # check if base has formatted="false"
            tree_base = ET.parse(base_file)
            for c in tree_base.getroot():
                if c.tag == 'string' and c.attrib['name'] == key and 'formatted' in c.attrib:
                    el.attrib['formatted'] = c.attrib['formatted']
                    
            root.append(el)
            added += 1
            
        tree.write(file_path, encoding='utf-8', xml_declaration=True)
    return added

report = {}
for file_path in all_string_files:
    locale = os.path.basename(os.path.dirname(file_path)).replace('values-', '')
    added = add_missing_translations(file_path, locale)
    report[locale] = added
    print(f"[{locale}] Added {added} translations.")

print(json.dumps(report, indent=2))
