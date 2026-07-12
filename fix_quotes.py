import os
import xml.etree.ElementTree as ET

for root, _, files in os.walk('app/src/main/res'):
    if 'values' in root and 'strings.xml' in files:
        file_path = os.path.join(root, 'strings.xml')
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # Escape unescaped single quotes
            import re
            # Only match single quotes not preceded by backslash
            content = re.sub(r"(?<!\\)'", r"\'", content)
            
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
        except Exception as e:
            print(f"Failed to process {file_path}: {e}")
