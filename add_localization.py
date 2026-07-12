with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

replacement = """        "Получение данных..." -> {
            when (lang) {
                "ru" -> "Получение данных..."
                "uk" -> "Отримання даних..."
                "be" -> "Атрыманне дадзеных..."
                "de" -> "Daten abrufen..."
                "es" -> "Obteniendo datos..."
                "fr" -> "Obtention des données..."
                "it" -> "Recupero dati..."
                "pt" -> "Obtendo dados..."
                "da" -> "Henter data..."
                "fi" -> "Haetaan tietoja..."
                "kk" -> "Деректер алынуда..."
                "lt" -> "Gaunami duomenys..."
                "lv" -> "Datu iegūšana..."
                "sv" -> "Hämtar data..."
                else -> "Getting data..."
            }
        }
        "Температура" -> {
            when (lang) {
                "ru" -> "Температура"
                "uk" -> "Температура"
                "be" -> "Тэмпература"
                "de" -> "Temperatur"
                "es" -> "Temperatura"
                "fr" -> "Température"
                "it" -> "Temperatura"
                "pt" -> "Temperatura"
                "da" -> "Temperatur"
                "fi" -> "Lämpötila"
                "kk" -> "Температура"
                "lt" -> "Temperatūra"
                "lv" -> "Temperatūra"
                "sv" -> "Temperatur"
                else -> "Temperature"
            }
        }
        "недоступна" -> {
            when (lang) {
                "ru" -> "недоступна"
                "uk" -> "недоступна"
                "be" -> "недаступна"
                "de" -> "nicht verfügbar"
                "es" -> "no disponible"
                "fr" -> "indisponible"
                "it" -> "non disponibile"
                "pt" -> "indisponível"
                "da" -> "ikke tilgængelig"
                "fi" -> "ei saatavilla"
                "kk" -> "қолжетімсіз"
                "lt" -> "neprieinama"
                "lv" -> "nav pieejams"
                "sv" -> "ej tillgänglig"
                else -> "unavailable"
            }
        }"""

content = content.replace("""        "Получение данных..." -> {
            when (lang) {
                "ru" -> "Получение данных..."
                "uk" -> "Отримання даних..."
                "be" -> "Атрыманне дадзеных..."
                "de" -> "Daten abrufen..."
                "es" -> "Obteniendo datos..."
                "fr" -> "Obtention des données..."
                "it" -> "Recupero dati..."
                "pt" -> "Obtendo dados..."
                "da" -> "Henter data..."
                "fi" -> "Haetaan tietoja..."
                "kk" -> "Деректер алынуда..."
                "lt" -> "Gaunami duomenys..."
                "lv" -> "Datu iegūšana..."
                "sv" -> "Hämtar data..."
                else -> "Getting data..."
            }
        }""", replacement)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
