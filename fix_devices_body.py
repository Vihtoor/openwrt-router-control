import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

append_texts = {
    "ru": "Список устройств также можно отобразить нажав на \"Загрузка\" и \"Выгрузка\" на графике трафика и загрузки процессора на главном экране приложения.",
    "uk": "Список пристроїв також можна відобразити натиснувши на \"Завантаження\" та \"Віддача\" на графіку трафіку та завантаження процесора на головному екрані програми.",
    "be": "Спіс прылад таксама можна адлюстраваць націснуўшы на \"Загрузка\" і \"Выгрузка\" на графіку трафіку і загрузкі працэсара на галоўным экране праграмы.",
    "de": "Die Geräteliste kann auch durch Klicken auf \"Download\" und \"Upload\" im Diagramm für Datenverkehr und CPU-Auslastung auf dem Hauptbildschirm der App angezeigt werden.",
    "es": "La lista de dispositivos también se puede mostrar haciendo clic en \"Descarga\" y \"Carga\" en el gráfico de tráfico y uso de CPU en la pantalla principal de la aplicación.",
    "fr": "La liste des appareils peut également être affichée en cliquant sur \"Téléchargement\" et \"Envoi\" sur le graphique du trafic et de l'utilisation du processeur sur l'écran principal de l'application.",
    "it": "L'elenco dei dispositivi può essere visualizzato anche facendo clic su \"Download\" e \"Upload\" nel grafico del traffico e dell'utilizzo della CPU nella schermata principale dell'app.",
    "pt": "A lista de dispositivos também pode ser exibida clicando em \"Download\" e \"Upload\" no gráfico de tráfego e uso da CPU na tela principal do aplicativo.",
    "da": "Enhedslisten kan også vises ved at klikke på \"Download\" og \"Upload\" på grafen for trafik og CPU-forbrug på appens hovedskærm.",
    "fi": "Laiteluettelo voidaan näyttää myös napsauttamalla \"Lataus\" ja \"Lähetys\" liikenteen ja suorittimen käyttöasteen kaaviossa sovelluksen päänäytössä.",
    "kk": "Құрылғылар тізімін қолданбаның негізгі экранындағы трафик және процессор жүктемесі графигіндегі \"Жүктеп алу\" және \"Жүктеп салу\" түймелерін басу арқылы да көрсетуге болады.",
    "lt": "Įrenginių sąrašą taip pat galima pamatyti pagrindiniame programos ekrane esančiame srauto ir procesoriaus apkrovos grafike paspaudus „Atsisiuntimas“ ir „Išsiuntimas“.",
    "lv": "Ierīču sarakstu var parādīt arī, noklikšķinot uz \"Lejupielāde\" un \"Augšupielāde\" trafika un CPU lietojuma diagrammā lietotnes galvenajā ekrānā.",
    "sv": "Enhetslistan kan också visas genom att klicka på \"Nedladdning\" och \"Uppladdning\" i diagrammet för trafik och CPU-användning på appens huvudskärm.",
    "en": "The device list can also be displayed by clicking on \"Download\" and \"Upload\" on the traffic and CPU usage graph on the main screen of the app."
}

def replace_lang(match):
    lang = match.group(1)
    text = match.group(2)
    # The text already ends with \n\n$ethernetNote.
    # We want to insert the new text before \n\n$ethernetNote or after it?
    # "В конце описания карточки Устройства добавь..." 
    # That implies at the very end of the description. So after ethernetNote.
    # Let's add it right at the end of the text.
    
    append_str = append_texts.get(lang, append_texts["en"])
    
    # Actually, we can add it as a new paragraph.
    return f'"{lang}" -> "{text}\\n\\n{append_str}"'

# We'll just replace the lines inside `when (normalizedLang)` 
# that start with "lang" -> "**..."

pattern = r'"([a-z]{2})" -> "(.*?\$\$ethernetNote)"'
# Need to be careful with $, it might not match properly in regex if not escaped, but ethernetNote is literally $ethernetNote

import re

for lang, txt in append_texts.items():
    if lang == "en":
        content = re.sub(r'else -> "(.*?\$\$ethernetNote)"'.replace('$$', '$'), f'else -> "\\g<1>\\n\\n{txt}"', content)
    else:
        content = re.sub(f'"{lang}" -> "(.*?\$\$ethernetNote)"'.replace('$$', '$'), f'"{lang}" -> "\\g<1>\\n\\n{txt}"', content)


with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)

print("done")
