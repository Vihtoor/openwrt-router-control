import os
import re
import xml.etree.ElementTree as ET
import glob

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

# Read all java, kt, xml files content once
code_content = ""
for root_dir, _, files in os.walk('app/src/main'):
    for f in files:
        if f.endswith('.java') or f.endswith('.kt') or f.endswith('.xml'):
            with open(os.path.join(root_dir, f), 'r', encoding='utf-8', errors='ignore') as file:
                code_content += file.read() + "\n"

unused_keys = []
for key in base_data.keys():
    if f'R.string.{key}' not in code_content and f'@string/{key}' not in code_content:
        # Some keys might be dynamic, but in this app they are likely not.
        unused_keys.append(key)

print(f"Total base keys: {len(base_data)}")
print(f"Unused keys found: {len(unused_keys)}")
print(f"Unused keys: {unused_keys}")

for file_path in all_string_files:
    locale = os.path.basename(os.path.dirname(file_path)).replace('values-', '')
    keys = get_keys_and_values(file_path)
    missing = set(base_data.keys()) - set(keys.keys()) - set(unused_keys)
    orphaned = set(keys.keys()) - set(base_data.keys())
    print(f"[{locale}] Missing: {list(missing)}")
    print(f"[{locale}] Orphaned: {list(orphaned)}")
    # Check placeholders
    for k in keys:
        if k in base_data:
            base_pl = re.findall(r'%[s d]|%\d\$[sd]', base_data[k] or "")
            loc_pl = re.findall(r'%[s d]|%\d\$[sd]', keys[k] or "")
            if sorted(base_pl) != sorted(loc_pl):
                print(f"[{locale}] Placeholder mismatch for '{k}': Base: {base_pl}, Loc: {loc_pl}")

