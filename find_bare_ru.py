import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r', encoding='utf-8') as f:
    content = f.read()

# Find strings in Text("...")
texts = re.findall(r'Text\(\s*"([^"\\]*[а-яА-ЯёЁ][^"\\]*)"', content)
texts += re.findall(r'text\s*=\s*"([^"\\]*[а-яА-ЯёЁ][^"\\]*)"', content)

# Find contentDescription
descs = re.findall(r'contentDescription\s*=\s*"([^"\\]*[а-яА-ЯёЁ][^"\\]*)"', content)

# Find label
labels = re.findall(r'label\s*=\s*"([^"\\]*[а-яА-ЯёЁ][^"\\]*)"', content)

all_bare = set(texts + descs + labels)

translated_keys = re.findall(r'"([^"]+)"\s*->\s*when\s*\(lang\)', content)
translated_keys = set(translated_keys)

missing_bare = all_bare - translated_keys
print("Missing bare strings:")
for m in sorted(missing_bare):
    print(m)

