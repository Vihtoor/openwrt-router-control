import os
import xml.etree.ElementTree as ET

translations = {
    'values': 'Today:',
    'values-ru': 'сегодня:',
    'values-uk': 'сьогодні:',
    'values-pt': 'Hoje:',
    'values-sv': 'Idag:',
    'values-lv': 'Šodien:',
    'values-lt': 'Šiandien:'
}

for val_dir, label in translations.items():
    strings_path = f"app/src/main/res/{val_dir}/strings.xml"
    if os.path.exists(strings_path):
        with open(strings_path, "r", encoding="utf-8") as f:
            content = f.read()
        
        import re
        content = re.sub(r'<string name="traffic_label">.*?</string>', f'<string name="traffic_label">{label}</string>', content)
        
        with open(strings_path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Updated {val_dir}")
    else:
        print(f"Not found {val_dir}")

