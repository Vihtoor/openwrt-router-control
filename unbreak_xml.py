import os
import re

base_dir = "app/src/main/res"

for folder in os.listdir(base_dir):
    if not folder.startswith("values"): continue
    
    xml_path = os.path.join(base_dir, folder, "strings.xml")
    if not os.path.exists(xml_path): continue
    
    with open(xml_path, "r") as f:
        content = f.read()
    
    # It might have made \\\' instead of \'
    content = content.replace("\\\\'", "\\'")
    # Some strings might have been completely unescaped and became escaped now.
    # But double check for ' without slash
    
    # Actually, Android resource XML apostrophes can just be enclosed in double quotes: "l'apostrophe"
    # Or escaped as \'
    
    with open(xml_path, "w") as f:
        f.write(content)

print("Fixed xml")
