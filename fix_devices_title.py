import os

titles = {
    "ru": "Устройства",
    "uk": "Пристрої",
    "be": "Прылады",
    "de": "Geräte",
    "es": "Dispositivos",
    "fr": "Appareils",
    "it": "Dispositivi",
    "pt": "Dispositivos",
    "da": "Enheder",
    "fi": "Laitteet",
    "kk": "Құрылғылар",
    "lt": "Įrenginiai",
    "lv": "Ierīces",
    "sv": "Enheter",
    "en": "Devices"
}

def get_lang(dirname):
    if dirname == "values": return "en"
    return dirname.replace("values-", "")

for root, dirs, files in os.walk("app/src/main/res"):
    for file in files:
        if file == "strings.xml":
            path = os.path.join(root, file)
            lang = get_lang(os.path.basename(root))
            title = titles.get(lang, titles["en"])
            
            with open(path, "r") as f:
                content = f.read()
                
            import re
            content = re.sub(r'<string name="devices_title">.*?</string>', f'<string name="devices_title">{title}</string>', content)
            
            with open(path, "w") as f:
                f.write(content)

print("done")
