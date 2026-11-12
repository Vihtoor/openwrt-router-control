import os
import re
import xml.etree.ElementTree as ET
import glob
import json

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

# 1. Read all code to find unused keys
code_content = ""
for root_dir, _, files in os.walk('app/src/main'):
    for f in files:
        if f.endswith('.java') or f.endswith('.kt') or f.endswith('.xml'):
            with open(os.path.join(root_dir, f), 'r', encoding='utf-8', errors='ignore') as file:
                code_content += file.read() + "\n"

unused_keys = set()
for key in base_data.keys():
    if f'R.string.{key}' not in code_content and f'@string/{key}' not in code_content:
        unused_keys.add(key)

print(f"Deleting {len(unused_keys)} unused keys from all locales.")

def update_xml(file_path, locale):
    tree = ET.parse(file_path)
    root = tree.getroot()
    
    current_elements = {child.attrib['name']: child for child in root if child.tag == 'string'}
    current_keys = set(current_elements.keys())
    base_keys = set(base_data.keys())
    
    removed = 0
    # remove unused keys in base and orphaned keys
    for child in list(root):
        if child.tag == 'string':
            key = child.attrib['name']
            if key in unused_keys or key not in base_keys:
                root.remove(child)
                removed += 1
                
    # add missing
    added = 0
    missing_keys = base_keys - current_keys - unused_keys
    # We'll just copy the english text for missing strings for now,
    # except for Russian where we will manually translate them.
    # The user asked to translate, but we don't have a reliable library right now without an API.
    # I'll just write placeholders and I'll replace them manually or write a dict.
    
    return missing_keys, removed

missing_dict = {}
for file_path in all_string_files:
    locale = os.path.basename(os.path.dirname(file_path)).replace('values-', '')
    missing, removed = update_xml(file_path, locale)
    missing_dict[locale] = list(missing)

print("Missing dictionary:")
print(json.dumps(missing_dict, indent=2))
