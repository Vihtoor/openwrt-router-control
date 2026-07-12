import os
import re
import glob

# Find all strings.xml
for strings_path in glob.glob("app/src/main/res/values*/strings.xml"):
    with open(strings_path, "r", encoding="utf-8") as f:
        content = f.read()
    
    # Try to find the translation for 'today' from today_traffic_kb
    match = re.search(r'<string name="today_traffic_kb">([^\s%]+)\s+%.1f', content)
    if match:
        today_word = match.group(1)
        # Make it lowercase for Russian, Ukrainian, Belarusian, Kazakh just in case, but let's keep original case and maybe capitalize for others.
        # Actually the user explicitly asked for "сегодня" lowercase in Russian: "вместо слова "Трафик" должно быть слово "сегодня""
        # So we can just append ':' to today_word.
        new_label = f"{today_word}:"
        
        # Replace traffic_label
        content = re.sub(
            r'<string name="traffic_label">.*?</string>', 
            f'<string name="traffic_label">{new_label}</string>', 
            content
        )
        
        with open(strings_path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Updated {strings_path} with label: {new_label}")
    else:
        print(f"Could not find today word in {strings_path}")

