import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

func = """    fun getDevicesDesc(lang: String): String {
        return when (lang) {
            "ru" -> "Список беспроводных и проводных устройств, подключенных к роутеру"
            "uk" -> "Список бездротових та дротових пристроїв, підключених до маршрутизатора"
            "be" -> "Спіс бесправадных і правадных прылад, падлучаных да маршрутызатара"
            "de" -> "Liste der mit dem Router verbundenen drahtlosen und kabelgebundenen Geräte"
            "es" -> "Lista de dispositivos inalámbricos y con cable conectados al enrutador"
            "fr" -> "Liste des appareils sans fil et filaires connectés au routeur"
            "it" -> "Elenco dei dispositivi wireless e cablati collegati al router"
            "pt" -> "Lista de dispositivos sem fio e com fio conectados ao roteador"
            "da" -> "Liste over trådløse og kablede enheder forbundet til routeren"
            "fi" -> "Luettelo reitittimeen yhdistetyistä langattomista ja langallisista laitteista"
            "kk" -> "Маршрутизаторға қосылған сымсыз және сымды құрылғылардың тізімі"
            "lt" -> "Belaidžių ir laidinių įrenginių, prijungtų prie maršrutizatoriaus, sąrašas"
            "lv" -> "Bezvadu un vārtejās ierīču, kas pievienotas maršrutētājam, saraksts"
            "sv" -> "Lista över trådlösa och trådbundna enheter anslutna till routern"
            else -> "List of wireless and wired devices connected to the router"
        }
    }

"""

# find `fun getDialogTitle` and insert before it
content = content.replace("    fun getDialogTitle", func + "    fun getDialogTitle")

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    main = f.read()

# I will replace inside MainActivity:
search = """                        Column {
                            Text(
                                text = context.getString(R.string.devices_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }"""
replace = """                        Column {
                            Text(
                                text = context.getString(R.string.devices_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = TestTabLocalizations.getDevicesDesc(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }"""
main = main.replace(search, replace)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(main)

print("done")
