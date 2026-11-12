import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r', encoding='utf-8') as f:
    content = f.read()

with open('app/src/main/java/com/example/ui/SettingsPanel.kt', 'r', encoding='utf-8') as f:
    content2 = f.read()
    
# Extract all Russian strings (between double quotes, containing at least one Russian letter)
ru_strings = set(re.findall(r'"([^"\\]*[а-яА-ЯёЁ][^"\\]*)"', content + "\n" + content2))

# Extract all keys present in the when blocks
translated_keys = re.findall(r'"([^"]+)"\s*->\s*when\s*\(lang\)', content)
translated_keys = set(translated_keys)

missing = ru_strings - translated_keys
print(f"Total Russian strings found: {len(ru_strings)}")
print(f"Total translated keys: {len(translated_keys)}")
print(f"Missing in when blocks ({len(missing)}):")
for m in sorted(missing):
    print(m)

