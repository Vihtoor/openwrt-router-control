with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

append_texts = {
    "ru": "Список устройств также можно отобразить нажав на \\\"Загрузка\\\" и \\\"Выгрузка\\\" на графике трафика и загрузки процессора на главном экране приложения.",
    "uk": "Список пристроїв також можна відобразити натиснувши на \\\"Завантаження\\\" та \\\"Віддача\\\" на графіку трафіку та завантаження процесора на головному екрані програми.",
    "be": "Спіс прылад таксама можна адлюстраваць націснуўшы на \\\"Загрузка\\\" і \\\"Выгрузка\\\" на графіку трафіку і загрузкі працэсара на галоўным экране праграмы.",
    "de": "Die Geräteliste kann auch durch Klicken auf \\\"Download\\\" und \\\"Upload\\\" im Diagramm für Datenverkehr und CPU-Auslastung auf dem Hauptbildschirm der App angezeigt werden.",
    "es": "La lista de dispositivos también se puede mostrar haciendo clic en \\\"Descarga\\\" y \\\"Carga\\\" en el gráfico de tráfico y uso de CPU en la pantalla principal de la aplicación.",
    "fr": "La liste des appareils peut également être affichée en cliquant sur \\\"Téléchargement\\\" et \\\"Envoi\\\" sur le graphique du trafic et de l'utilisation du processeur sur l'écran principal de l'application.",
    "it": "L'elenco dei dispositivi può essere visualizzato anche facendo clic su \\\"Download\\\" e \\\"Upload\\\" nel grafico del traffico e dell'utilizzo della CPU nella schermata principale dell'app.",
    "pt": "A lista de dispositivos também pode ser exibida clicando em \\\"Download\\\" e \\\"Upload\\\" no gráfico de tráfego e uso da CPU na tela principal do aplicativo.",
    "da": "Enhedslisten kan også vises ved at klikke på \\\"Download\\\" og \\\"Upload\\\" på grafen for trafik og CPU-forbrug på appens hovedskærm.",
    "fi": "Laiteluettelo voidaan näyttää myös napsauttamalla \\\"Lataus\\\" ja \\\"Lähetys\\\" liikenteen ja suorittimen käyttöasteen kaaviossa sovelluksen päänäytössä.",
    "kk": "Құрылғылар тізімін қолданбаның негізгі экранындағы трафик және процессор жүктемесі графигіндегі \\\"Жүктеп алу\\\" және \\\"Жүктеп салу\\\" түймелерін басу арқылы да көрсетуге болады.",
    "lt": "Įrenginių sąrašą taip pat galima pamatyti pagrindiniame programos ekrane esančiame srauto ir procesoriaus apkrovos grafike paspaudus „Atsisiuntimas“ ir „Išsiuntimas“.",
    "lv": "Ierīču sarakstu var parādīt arī, noklikšķinot uz \\\"Lejupielāde\\\" un \\\"Augšupielāde\\\" trafika un CPU lietojuma diagrammā lietotnes galvenajā ekrānā.",
    "sv": "Enhetslistan kan också visas genom att klicka på \\\"Nedladdning\\\" och \\\"Uppladdning\\\" i diagrammet för trafik och CPU-användning på appens huvudskärm.",
    "en": "The device list can also be displayed by clicking on \\\"Download\\\" and \\\"Upload\\\" on the traffic and CPU usage graph on the main screen of the app."
}

import re

for lang, text in append_texts.items():
    if lang == "en":
        search = 'else -> "**Device List:**\\nThis tool displays a list of all devices connected to your router. You can see their IP and MAC addresses, connection type (Wi-Fi or Ethernet), as well as traffic consumption statistics and current speed.\\n\\n$ethernetNote"'
        replace = f'else -> "**Device List:**\\nThis tool displays a list of all devices connected to your router. You can see their IP and MAC addresses, connection type (Wi-Fi or Ethernet), as well as traffic consumption statistics and current speed.\\n\\n$ethernetNote\\n\\n{text}"'
        content = content.replace(search, replace)
    else:
        # e.g. "ru" -> "**Список устройств:**...
        # We need to find the exact line. Let's just do a regex replace on each language key inside the block.
        # It's easier:
        pattern = f'"{lang}" -> "(.*?)\\\\\\$ethernetNote"'
        # Wait, the string in kotlin is "$ethernetNote". In python `content` it's `\$ethernetNote` or `$ethernetNote`.
        # Let's just find `$ethernetNote"` and replace it with `$ethernetNote\\n\\n{text}"` but we need to do it per language.
        pass

# Let's do it manually since the lines are right there.
lines = content.split('\n')
for i in range(len(lines)):
    if ' -> "**' in lines[i] and '$ethernetNote"' in lines[i]:
        # Extract the language
        m = re.search(r'"([a-z]{2})" ->', lines[i])
        if m:
            lang = m.group(1)
            txt = append_texts.get(lang, append_texts["en"])
            lines[i] = lines[i].replace('$ethernetNote"', f'$ethernetNote\\n\\n{txt}"')
        elif 'else ->' in lines[i]:
            txt = append_texts["en"]
            lines[i] = lines[i].replace('$ethernetNote"', f'$ethernetNote\\n\\n{txt}"')

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write('\n'.join(lines))
