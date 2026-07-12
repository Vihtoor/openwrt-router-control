import os
import xml.etree.ElementTree as ET

translations = {
    "en": { # default 'values'
        "devices_title": "Devices",
        "select_device_for_chart": "Select a device to view its chart",
        "click_device_for_chart": "Tap a device to view its chart",
        "no_connected_devices": "No connected devices or data unavailable.",
        "connected_devices_count": "%1$d devices connected",
        "link_speed": "Link speed:",
        "port_mapping_warning": "Warning: for this connection type, the total traffic of the router's physical port is shown, not just this device.",
        "traffic_not_available": "Traffic not available on this router",
        "download_speed_format": "Download: %.2f Mbps",
        "upload_speed_format": "Upload: %.2f Mbps",
        "today_traffic_kb": "today %.1f KB",
        "today_traffic_mb": "today %.1f MB",
        "today_traffic_gb": "today %.1f GB",
        "traffic_label": "Traffic:"
    },
    "ru": {
        "devices_title": "Устройства",
        "select_device_for_chart": "Выберите устройство, чтобы открыть график",
        "click_device_for_chart": "Нажмите на устройство, чтобы открыть график",
        "no_connected_devices": "Нет подключенных устройств или данные недоступны.",
        "connected_devices_count": "Подключено %1$d устройств",
        "link_speed": "Скорость линка:",
        "port_mapping_warning": "Внимание: для этого типа подключения показан общий трафик всего физического порта роутера, а не только этого устройства.",
        "traffic_not_available": "Трафик недоступен на этом роутере",
        "download_speed_format": "Загрузка: %.2f Мбит/с",
        "upload_speed_format": "Выгрузка: %.2f Мбит/с",
        "today_traffic_kb": "сегодня %.1f КБ",
        "today_traffic_mb": "сегодня %.1f МБ",
        "today_traffic_gb": "сегодня %.1f ГБ",
        "traffic_label": "Трафик:"
    },
    "be": {
        "devices_title": "Прылады",
        "select_device_for_chart": "Выберыце прыладу, каб адкрыць графік",
        "click_device_for_chart": "Націсніце на прыладу, каб адкрыць графік",
        "no_connected_devices": "Няма падлучаных прылад або дадзеныя недаступныя.",
        "connected_devices_count": "Падлучана %1$d прылад",
        "link_speed": "Хуткасць лінка:",
        "port_mapping_warning": "Увага: для гэтага тыпу падлучэння паказаны агульны трафік усяго фізічнага порта роўтара, а не толькі гэтай прылады.",
        "traffic_not_available": "Трафік недаступны на гэтым роўтары",
        "download_speed_format": "Загрузка: %.2f Мбіт/с",
        "upload_speed_format": "Выгрузка: %.2f Мбіт/с",
        "today_traffic_kb": "сёння %.1f КБ",
        "today_traffic_mb": "сёння %.1f МБ",
        "today_traffic_gb": "сёння %.1f ГБ",
        "traffic_label": "Трафік:"
    },
    "uk": {
        "devices_title": "Пристрої",
        "select_device_for_chart": "Виберіть пристрій, щоб відкрити графік",
        "click_device_for_chart": "Натисніть на пристрій, щоб відкрити графік",
        "no_connected_devices": "Немає підключених пристроїв або дані недоступні.",
        "connected_devices_count": "Підключено %1$d пристроїв",
        "link_speed": "Швидкість лінку:",
        "port_mapping_warning": "Увага: для цього типу підключення показано загальний трафік усього фізичного порту роутера, а не лише цього пристрою.",
        "traffic_not_available": "Трафік недоступний на цьому роутері",
        "download_speed_format": "Завантаження: %.2f Мбіт/с",
        "upload_speed_format": "Віддача: %.2f Мбіт/с",
        "today_traffic_kb": "сьогодні %.1f КБ",
        "today_traffic_mb": "сьогодні %.1f МБ",
        "today_traffic_gb": "сьогодні %.1f ГБ",
        "traffic_label": "Трафік:"
    },
    "kk": {
        "devices_title": "Құрылғылар",
        "select_device_for_chart": "Графикті ашу үшін құрылғыны таңдаңыз",
        "click_device_for_chart": "Графикті ашу үшін құрылғыны басыңыз",
        "no_connected_devices": "Қосылған құрылғылар жоқ немесе деректер қолжетімсіз.",
        "connected_devices_count": "%1$d құрылғы қосылған",
        "link_speed": "Байланыс жылдамдығы:",
        "port_mapping_warning": "Ескерту: бұл қосылым түрі үшін тек осы құрылғының емес, роутердің барлық физикалық портының жалпы трафигі көрсетілген.",
        "traffic_not_available": "Бұл роутерде трафик қолжетімсіз",
        "download_speed_format": "Жүктеу: %.2f Мбит/с",
        "upload_speed_format": "Жіберу: %.2f Мбит/с",
        "today_traffic_kb": "бүгін %.1f КБ",
        "today_traffic_mb": "бүгін %.1f МБ",
        "today_traffic_gb": "бүгін %.1f ГБ",
        "traffic_label": "Трафик:"
    },
    "es": {
        "devices_title": "Dispositivos",
        "select_device_for_chart": "Seleccione un dispositivo para ver su gráfico",
        "click_device_for_chart": "Toque un dispositivo para ver su gráfico",
        "no_connected_devices": "No hay dispositivos conectados o datos no disponibles.",
        "connected_devices_count": "%1$d dispositivos conectados",
        "link_speed": "Velocidad de enlace:",
        "port_mapping_warning": "Advertencia: para este tipo de conexión, se muestra el tráfico total del puerto físico del enrutador, no solo este dispositivo.",
        "traffic_not_available": "Tráfico no disponible en este enrutador",
        "download_speed_format": "Descarga: %.2f Mbps",
        "upload_speed_format": "Subida: %.2f Mbps",
        "today_traffic_kb": "hoy %.1f KB",
        "today_traffic_mb": "hoy %.1f MB",
        "today_traffic_gb": "hoy %.1f GB",
        "traffic_label": "Tráfico:"
    },
    "fr": {
        "devices_title": "Appareils",
        "select_device_for_chart": "Sélectionnez un appareil pour voir son graphique",
        "click_device_for_chart": "Touchez un appareil pour voir son graphique",
        "no_connected_devices": "Aucun appareil connecté ou données indisponibles.",
        "connected_devices_count": "%1$d appareils connectés",
        "link_speed": "Vitesse de liaison:",
        "port_mapping_warning": "Attention: pour ce type de connexion, le trafic total du port physique du routeur est affiché, pas seulement cet appareil.",
        "traffic_not_available": "Trafic indisponible sur ce routeur",
        "download_speed_format": "Téléchargement: %.2f Mbps",
        "upload_speed_format": "Envoi: %.2f Mbps",
        "today_traffic_kb": "aujourd'hui %.1f Ko",
        "today_traffic_mb": "aujourd'hui %.1f Mo",
        "today_traffic_gb": "aujourd'hui %.1f Go",
        "traffic_label": "Trafic:"
    },
    "de": {
        "devices_title": "Geräte",
        "select_device_for_chart": "Wählen Sie ein Gerät aus, um dessen Diagramm anzuzeigen",
        "click_device_for_chart": "Tippen Sie auf ein Gerät, um dessen Diagramm anzuzeigen",
        "no_connected_devices": "Keine verbundenen Geräte oder Daten nicht verfügbar.",
        "connected_devices_count": "%1$d Geräte verbunden",
        "link_speed": "Verbindungsgeschwindigkeit:",
        "port_mapping_warning": "Achtung: Für diesen Verbindungstyp wird der gesamte Datenverkehr des physischen Ports des Routers angezeigt, nicht nur dieses Gerät.",
        "traffic_not_available": "Datenverkehr auf diesem Router nicht verfügbar",
        "download_speed_format": "Download: %.2f Mbps",
        "upload_speed_format": "Upload: %.2f Mbps",
        "today_traffic_kb": "heute %.1f KB",
        "today_traffic_mb": "heute %.1f MB",
        "today_traffic_gb": "heute %.1f GB",
        "traffic_label": "Datenverkehr:"
    },
    "it": {
        "devices_title": "Dispositivi",
        "select_device_for_chart": "Seleziona un dispositivo per visualizzare il grafico",
        "click_device_for_chart": "Tocca un dispositivo per visualizzare il grafico",
        "no_connected_devices": "Nessun dispositivo connesso o dati non disponibili.",
        "connected_devices_count": "%1$d dispositivi connessi",
        "link_speed": "Velocità di collegamento:",
        "port_mapping_warning": "Attenzione: per questo tipo di connessione, viene mostrato il traffico totale del porto fisico del router, non solo questo dispositivo.",
        "traffic_not_available": "Traffico non disponibile su questo router",
        "download_speed_format": "Download: %.2f Mbps",
        "upload_speed_format": "Upload: %.2f Mbps",
        "today_traffic_kb": "oggi %.1f KB",
        "today_traffic_mb": "oggi %.1f MB",
        "today_traffic_gb": "oggi %.1f GB",
        "traffic_label": "Traffico:"
    },
    "pt": {
        "devices_title": "Dispositivos",
        "select_device_for_chart": "Selecione um dispositivo para ver seu gráfico",
        "click_device_for_chart": "Toque em um dispositivo para ver seu gráfico",
        "no_connected_devices": "Nenhum dispositivo conectado ou dados indisponíveis.",
        "connected_devices_count": "%1$d dispositivos conectados",
        "link_speed": "Velocidade da ligação:",
        "port_mapping_warning": "Aviso: para este tipo de conexão, o tráfego total do porta física do roteador é mostrado, não apenas este dispositivo.",
        "traffic_not_available": "Tráfego não disponível neste roteador",
        "download_speed_format": "Download: %.2f Mbps",
        "upload_speed_format": "Upload: %.2f Mbps",
        "today_traffic_kb": "hoje %.1f KB",
        "today_traffic_mb": "hoje %.1f MB",
        "today_traffic_gb": "hoje %.1f GB",
        "traffic_label": "Tráfego:"
    },
    "da": {
        "devices_title": "Enheder",
        "select_device_for_chart": "Vælg en enhed for at se dens diagram",
        "click_device_for_chart": "Tryk på en enhed for at se dens diagram",
        "no_connected_devices": "Ingen tilsluttede enheder eller data utilgængelige.",
        "connected_devices_count": "%1$d enheder tilsluttet",
        "link_speed": "Linkhastighed:",
        "port_mapping_warning": "Advarsel: for denne forbindelsestype vises den samlede trafik på routerens fysiske port, ikke kun denne enhed.",
        "traffic_not_available": "Trafik ikke tilgængelig på denne router",
        "download_speed_format": "Download: %.2f Mbps",
        "upload_speed_format": "Upload: %.2f Mbps",
        "today_traffic_kb": "i dag %.1f KB",
        "today_traffic_mb": "i dag %.1f MB",
        "today_traffic_gb": "i dag %.1f GB",
        "traffic_label": "Trafik:"
    },
    "fi": {
        "devices_title": "Laitteet",
        "select_device_for_chart": "Valitse laite nähdäksesi sen kaavion",
        "click_device_for_chart": "Napauta laitetta nähdäksesi sen kaavion",
        "no_connected_devices": "Ei yhdistettyjä laitteita tai tietoja ei saatavilla.",
        "connected_devices_count": "%1$d laitetta yhdistetty",
        "link_speed": "Linkin nopeus:",
        "port_mapping_warning": "Varoitus: tässä yhteystyypissä näytetään reitittimen fyysisen portin kokonaisliikenne, ei vain tätä laitetta.",
        "traffic_not_available": "Liikenne ei ole saatavilla tällä reitittimellä",
        "download_speed_format": "Lataus: %.2f Mbps",
        "upload_speed_format": "Lähetys: %.2f Mbps",
        "today_traffic_kb": "tänään %.1f KB",
        "today_traffic_mb": "tänään %.1f MB",
        "today_traffic_gb": "tänään %.1f GB",
        "traffic_label": "Liikenne:"
    },
    "sv": {
        "devices_title": "Enheter",
        "select_device_for_chart": "Välj en enhet för att se dess diagram",
        "click_device_for_chart": "Tryck på en enhet för att se dess diagram",
        "no_connected_devices": "Inga anslutna enheter eller data otillgängliga.",
        "connected_devices_count": "%1$d enheter anslutna",
        "link_speed": "Länkhastighet:",
        "port_mapping_warning": "Varning: för denna anslutningstyp visas den totala trafiken på routerns fysiska port, inte bara den här enheten.",
        "traffic_not_available": "Trafik ej tillgänglig på denna router",
        "download_speed_format": "Nedladdning: %.2f Mbps",
        "upload_speed_format": "Uppladdning: %.2f Mbps",
        "today_traffic_kb": "idag %.1f KB",
        "today_traffic_mb": "idag %.1f MB",
        "today_traffic_gb": "idag %.1f GB",
        "traffic_label": "Trafik:"
    },
    "lt": {
        "devices_title": "Įrenginiai",
        "select_device_for_chart": "Pasirinkite įrenginį, kad pamatytumėte jo grafiką",
        "click_device_for_chart": "Palieskite įrenginį, kad pamatytumėte jo grafiką",
        "no_connected_devices": "Nėra prijungtų įrenginių arba duomenys nepasiekiami.",
        "connected_devices_count": "%1$d įrenginių prijungta",
        "link_speed": "Ryšio greitis:",
        "port_mapping_warning": "Įspėjimas: šiam ryšio tipui rodomas bendras maršrutizatoriaus fizinio prievado srautas, o ne tik šis įrenginys.",
        "traffic_not_available": "Srautas nepasiekiamas šiame maršrutizatoriuje",
        "download_speed_format": "Atsisiuntimas: %.2f Mbps",
        "upload_speed_format": "Išsiuntimas: %.2f Mbps",
        "today_traffic_kb": "šiandien %.1f KB",
        "today_traffic_mb": "šiandien %.1f MB",
        "today_traffic_gb": "šiandien %.1f GB",
        "traffic_label": "Srautas:"
    },
    "lv": {
        "devices_title": "Ierīces",
        "select_device_for_chart": "Izvēlieties ierīci, lai skatītu tās grafiku",
        "click_device_for_chart": "Pieskarieties ierīcei, lai skatītu tās grafiku",
        "no_connected_devices": "Nav pievienotu ierīču vai dati nav pieejami.",
        "connected_devices_count": "%1$d ierīces pievienotas",
        "link_speed": "Saites ātrums:",
        "port_mapping_warning": "Brīdinājums: šim savienojuma tipam tiek parādīta kopējā maršrutētāja fiziskā porta satiksme, nevis tikai šī ierīce.",
        "traffic_not_available": "Satiksme nav pieejama šajā maršrutētājā",
        "download_speed_format": "Lejupielāde: %.2f Mbps",
        "upload_speed_format": "Augšupielāde: %.2f Mbps",
        "today_traffic_kb": "šodien %.1f KB",
        "today_traffic_mb": "šodien %.1f MB",
        "today_traffic_gb": "šodien %.1f GB",
        "traffic_label": "Satiksme:"
    }
}

base_dir = "app/src/main/res"

for folder in os.listdir(base_dir):
    if not folder.startswith("values"): continue
    
    # skip night
    if "night" in folder: continue
    
    lang = folder.split("-")[1] if "-" in folder else "en"
    if lang not in translations:
        # fallback to english if not found
        lang = "en"
        
    xml_path = os.path.join(base_dir, folder, "strings.xml")
    if not os.path.exists(xml_path): continue
    
    tree = ET.parse(xml_path)
    root = tree.getroot()
    
    existing_keys = [child.attrib.get('name') for child in root if child.tag == 'string']
    
    for key, value in translations[lang].items():
        if key in existing_keys:
            for child in root:
                if child.tag == 'string' and child.attrib.get('name') == key:
                    child.text = value
        else:
            new_string = ET.SubElement(root, "string", name=key)
            new_string.text = value
            
    # Write back
    tree.write(xml_path, encoding="utf-8", xml_declaration=True)

print("Done translating!")
