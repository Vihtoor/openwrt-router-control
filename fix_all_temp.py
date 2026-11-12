import os
import re

res_dir = 'app/src/main/res/'
for root, dirs, files in os.walk(res_dir):
    for file in files:
        if file == 'strings.xml':
            filepath = os.path.join(root, file)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
            
            def repl(match):
                word = match.group(1)
                # Capitalize the first letter properly (Unicode aware in python)
                capitalized = word[0].upper() + word[1:]
                return f'<string name="label_temperature">{capitalized}</string>'
            
            new_content = re.sub(r'<string name="label_temperature">([^<]+)</string>', repl, content)
            
            if new_content != content:
                with open(filepath, 'w', encoding='utf-8') as f:
                    f.write(new_content)
                print(f"Updated {filepath}")
