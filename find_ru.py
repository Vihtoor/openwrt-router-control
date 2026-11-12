import re

files = ['app/src/main/java/com/example/MainActivity.kt', 'app/src/main/java/com/example/ui/SettingsPanel.kt']

for f in files:
    with open(f, 'r', encoding='utf-8') as file:
        content = file.read()
        
    # Find strings containing Cyrillic characters
    matches = set(re.findall(r'"([^"\\]*[а-яА-ЯёЁ][^"\\]*)"', content))
    
    if matches:
        print(f"--- {f} ---")
        for m in sorted(matches):
            print(m)
