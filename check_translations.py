import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r', encoding='utf-8') as f:
    content = f.read()

# Find all translateText("...", ...)
matches = re.findall(r'translateText\(\s*"([^"]+)"', content)

with open('app/src/main/java/com/example/ui/SettingsPanel.kt', 'r', encoding='utf-8') as f:
    content2 = f.read()

matches += re.findall(r'translateText\(\s*"([^"]+)"', content2)

matches = set(matches)
print(f"Total unique texts to translate: {len(matches)}")

# Extract all keys present in the when blocks
translated_keys = re.findall(r'"([^"]+)"\s*->\s*when\s*\(lang\)', content)
translated_keys = set(translated_keys)

print(f"Total translated keys: {len(translated_keys)}")

missing = matches - translated_keys
print(f"Missing in when blocks: {missing}")

