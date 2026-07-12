import os

for root, _, files in os.walk('app/src/main/res'):
    if 'values' in root and 'strings.xml' in files:
        file_path = os.path.join(root, 'strings.xml')
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            content = content.replace("<?xml version=\\'1.0\\' encoding=\\'utf-8\\'?>", '<?xml version="1.0" encoding="utf-8"?>')
            content = content.replace("<?xml version='1.0' encoding='utf-8'?>", '<?xml version="1.0" encoding="utf-8"?>')
            
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
        except Exception as e:
            pass
