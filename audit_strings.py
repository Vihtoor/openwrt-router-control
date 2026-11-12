import os
import re
import xml.etree.ElementTree as ET
import glob
from collections import defaultdict
import subprocess

res_dir = 'app/src/main/res'
base_file = os.path.join(res_dir, 'values/strings.xml')
all_string_files = glob.glob(os.path.join(res_dir, 'values-*/strings.xml'))

def get_keys(file_path):
    tree = ET.parse(file_path)
    root = tree.getroot()
    keys = {}
    for child in root:
        if child.tag == 'string':
            keys[child.attrib['name']] = child.text
    return keys

base_keys = get_keys(base_file)
all_keys_set = set(base_keys.keys())

for file_path in all_string_files:
    keys = get_keys(file_path)
    all_keys_set.update(keys.keys())

unused_keys = []
# Find unused keys in code
# we will grep for R.string.<key>
print("Checking for unused keys...")
for key in all_keys_set:
    res = subprocess.run(['grep', '-r', f'R.string.{key}', 'app/src/main/java'], capture_output=True, text=True)
    if not res.stdout.strip():
        unused_keys.append(key)

print(f"Unused keys found: {unused_keys}")

# Print report
for file_path in all_string_files:
    locale = os.path.basename(os.path.dirname(file_path)).replace('values-', '')
    keys = get_keys(file_path)
    
    missing = set(base_keys.keys()) - set(keys.keys())
    orphaned = set(keys.keys()) - set(base_keys.keys())
    identical = [k for k in keys if k in base_keys and keys[k] == base_keys[k]]
    
    print(f"--- Locale: {locale} ---")
    if missing:
        print(f"Missing: {missing}")
    if orphaned:
        print(f"Orphaned: {orphaned}")
    # if identical:
    #     print(f"Identical to base: {identical}")

