import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

functions_to_add = """
    fun getBannerNoBridge(lang: String): String {
        val normalizedLang = when (lang) {
            "ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv" -> lang
            else -> "en"
        }
        return when (normalizedLang) {
            "ru" -> "Не найдена утилита bridge — определение устройств на LAN-портах недоступно."
            "uk" -> "Не знайдена утиліта bridge — визначення пристроїв на LAN-портах недоступне."
            "be" -> "Не знойдзена ўтыліта bridge — вызначэнне прылад на LAN-партах недаступна."
            "de" -> "Das Dienstprogramm bridge wurde nicht gefunden — die Erkennung von Geräten an LAN-Ports ist nicht verfügbar."
            "es" -> "No se encontró la utilidad bridge: la detección de dispositivos en los puertos LAN no está disponible."
            "fr" -> "Utilitaire bridge introuvable — la détection des périphériques sur les ports LAN n'est pas disponible."
            "it" -> "Utilità bridge non trovata — il rilevamento dei dispositivi sulle porte LAN non è disponibile."
            "pt" -> "Utilitário bridge não encontrado — a detecção de dispositivos nas portas LAN não está disponível."
            "da" -> "Værktøjet bridge blev ikke fundet — registrering af enheder på LAN-porte er ikke tilgængelig."
            "fi" -> "Bridge-työkalua ei löydy — laitteiden tunnistus LAN-porteissa ei ole käytettävissä."
            "kk" -> "bridge утилитасы табылмады — LAN порттарында құрылғыларды анықтау қолжетімсіз."
            "lt" -> "Nerastas bridge įrankis — įrenginių aptikimas LAN prievaduose neprieinamas."
            "lv" -> "Nav atrasta bridge utilītprogramma — ierīču noteikšana LAN portos nav pieejama."
            "sv" -> "Verktyget bridge hittades inte — enhetsdetektering på LAN-portar är inte tillgänglig."
            else -> "bridge utility not found — device detection on LAN ports is unavailable."
        }
    }

    fun getBannerNoSwconfig(lang: String): String {
        val normalizedLang = when (lang) {
            "ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv" -> lang
            else -> "en"
        }
        return when (normalizedLang) {
            "ru" -> "Не найдена утилита swconfig — недоступен линк-статус и трафик LAN-портов (если роутер поддерживает swconfig)."
            "uk" -> "Не знайдена утиліта swconfig — недоступний лінк-статус і трафік LAN-портів (якщо маршрутизатор підтримує swconfig)."
            "be" -> "Не знойдзена ўтыліта swconfig — недаступны лінк-статус і трафік LAN-партоў (калі маршрутызатар падтрымлівае swconfig)."
            "de" -> "Das Dienstprogramm swconfig wurde nicht gefunden — Verbindungsstatus und Datenverkehr von LAN-Ports sind nicht verfügbar (sofern der Router swconfig unterstützt)."
            "es" -> "No se encontró la utilidad swconfig: el estado del enlace y el tráfico de los puertos LAN no están disponibles (si el enrutador admite swconfig)."
            "fr" -> "Utilitaire swconfig introuvable — l'état de la liaison et le trafic des ports LAN ne sont pas disponibles (si le routeur prend en charge swconfig)."
            "it" -> "Utilità swconfig non trovata — lo stato del collegamento e il traffico delle porte LAN non sono disponibili (se il router supporta swconfig)."
            "pt" -> "Utilitário swconfig não encontrado — o status do link e o tráfego das portas LAN não estão disponíveis (se o roteador suportar swconfig)."
            "da" -> "Værktøjet swconfig blev ikke fundet — linkstatus og trafik på LAN-porte er ikke tilgængelige (hvis routeren understøtter swconfig)."
            "fi" -> "Swconfig-työkalua ei löydy — linkin tila ja LAN-porttien liikenne eivät ole käytettävissä (jos reititin tukee swconfig-työkalua)."
            "kk" -> "swconfig утилитасы табылмады — LAN порттарының байланыс күйі мен трафигі қолжетімсіз (егер маршрутизатор swconfig-ті қолдаса)."
            "lt" -> "Nerastas swconfig įrankis — LAN prievadų ryšio būsena ir srautas neprieinami (jei maršrutizatorius palaiko swconfig)."
            "lv" -> "Nav atrasta swconfig utilītprogramma — LAN portu saites statuss un trafiks nav pieejams (ja maršrutētājs atbalsta swconfig)."
            "sv" -> "Verktyget swconfig hittades inte — länkstatus och trafik för LAN-portar är inte tillgängliga (om routern stöder swconfig)."
            else -> "swconfig utility not found — link status and traffic of LAN ports are unavailable (if the router supports swconfig)."
        }
    }

    fun getBannerNoBoardJson(lang: String): String {
        val normalizedLang = when (lang) {
            "ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv" -> lang
            else -> "en"
        }
        return when (normalizedLang) {
            "ru" -> "Невозможно сопоставить логические порты с физическими номерами LAN-портов."
            "uk" -> "Неможливо зіставити логічні порти з фізичними номерами LAN-портів."
            "be" -> "Немагчыма супаставіць лагічныя парты з фізічнымі нумарамі LAN-партоў."
            "de" -> "Logische Ports können nicht mit physischen LAN-Port-Nummern abgeglichen werden."
            "es" -> "No es posible mapear puertos lógicos con números de puertos LAN físicos."
            "fr" -> "Impossible de faire correspondre les ports logiques aux numéros de ports LAN physiques."
            "it" -> "Impossibile mappare le porte logiche ai numeri delle porte LAN fisiche."
            "pt" -> "Não é possível mapear portas lógicas para números de portas LAN físicas."
            "da" -> "Kan ikke matche logiske porte med fysiske LAN-portnumre."
            "fi" -> "Loogisia portteja ei voi yhdistää fyysisiin LAN-porttien numeroihin."
            "kk" -> "Логикалық порттарды физикалық LAN порт нөмірлерімен сәйкестендіру мүмкін емес."
            "lt" -> "Neįmanoma susieti loginių prievadų su fiziniais LAN prievadų numeriais."
            "lv" -> "Nevar saskaņot loģiskos portus ar fizisko LAN portu numuriem."
            "sv" -> "Kan inte matcha logiska portar med fysiska LAN-portnummer."
            else -> "Cannot map logical ports to physical LAN port numbers."
        }
    }

    fun getBannerUnsupported(lang: String, arch: String): String {
        val normalizedLang = when (lang) {
            "ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv" -> lang
            else -> "en"
        }
        return when (normalizedLang) {
            "ru" -> "Роутер/прошивка не поддерживает разделение LAN-портов (архитектура: $arch)."
            "uk" -> "Маршрутизатор/прошивка не підтримує поділ LAN-портів (архітектура: $arch)."
            "be" -> "Маршрутызатар/прашыўка не падтрымлівае падзел LAN-партоў (архітэктура: $arch)."
            "de" -> "Router/Firmware unterstützt keine Trennung von LAN-Ports (Architektur: $arch)."
            "es" -> "El enrutador/firmware no admite la separación de puertos LAN (arquitectura: $arch)."
            "fr" -> "Le routeur/micrologiciel ne prend pas en charge la séparation des ports LAN (architecture : $arch)."
            "it" -> "Il router/firmware non supporta la separazione delle porte LAN (architettura: $arch)."
            "pt" -> "O roteador/firmware não suporta a separação de portas LAN (arquitetura: $arch)."
            "da" -> "Router/firmware understøtter ikke adskillelse af LAN-porte (arkitektur: $arch)."
            "fi" -> "Reititin/laiteohjelmisto ei tue LAN-porttien erottelua (arkkitehtuuri: $arch)."
            "kk" -> "Маршрутизатор/бағдарламалық жасақтама LAN порттарын бөлуді қолдамайды (архитектура: $arch)."
            "lt" -> "Maršrutizatorius / programinė įranga nepalaiko LAN prievadų atskyrimo (architektūra: $arch)."
            "lv" -> "Maršrutētājs / aparātprogrammatūra neatbalsta LAN portu atdalīšanu (arhitektūra: $arch)."
            "sv" -> "Routern/firmware stöder inte separering av LAN-portar (arkitektur: $arch)."
            else -> "Router/firmware does not support LAN port separation (architecture: $arch)."
        }
    }

    fun getBannerInstallInstruction(lang: String): String {
        val normalizedLang = when (lang) {
            "ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv" -> lang
            else -> "en"
        }
        return when (normalizedLang) {
            "ru" -> "Вы можете установить утилиту для сбора статистики скопировав следующую команду и вставив ее в Консоль:"
            "uk" -> "Ви можете встановити утиліту для збору статистики, скопіювавши наступну команду і вставивши її в Консоль:"
            "be" -> "Вы можаце ўсталяваць утыліту для збору статыстыкі, скапіяваўшы наступную каманду і ўставіўшы яе ў Кансоль:"
            "de" -> "Sie können das Dienstprogramm zum Sammeln von Statistiken installieren, indem Sie den folgenden Befehl kopieren und in die Konsole einfügen:"
            "es" -> "Puede instalar la utilidad de recopilación de estadísticas copiando el siguiente comando y pegándolo en la Consola:"
            "fr" -> "Vous pouvez installer l'utilitaire de collecte de statistiques en copiant la commande suivante et en la collant dans la console :"
            "it" -> "È possibile installare l'utilità di raccolta statistiche copiando il seguente comando e incollandolo nella Console:"
            "pt" -> "Você pode instalar o utilitário de coleta de estatísticas copiando o seguinte comando e colando-o no Console:"
            "da" -> "Du kan installere hjælpeprogrammet til indsamling af statistik ved at kopiere følgende kommando og indsætte den i konsollen:"
            "fi" -> "Voit asentaa tilastojen keräystyökalun kopioimalla seuraavan komennon ja liittämällä sen konsoliin:"
            "kk" -> "Келесі пәрменді көшіріп, Консольге қою арқылы статистиканы жинау утилитасын орнатуға болады:"
            "lt" -> "Galite įdiegti statistikos rinkimo įrankį nukopijavę šią komandą ir įklijavę ją į konsolę:"
            "lv" -> "Jūs varat instalēt statistikas vākšanas utilītprogrammu, kopējot šo komandu un ielīmējot to konsolē:"
            "sv" -> "Du kan installera verktyget för att samla in statistik genom att kopiera följande kommando och klistra in det i konsolen:"
            else -> "You can install the utility for collecting statistics by copying the following command and pasting it into the Console:"
        }
    }

    fun getBannerGotIt(lang: String): String {
        val normalizedLang = when (lang) {
            "ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv" -> lang
            else -> "en"
        }
        return when (normalizedLang) {
            "ru" -> "Понятно"
            "uk" -> "Зрозуміло"
            "be" -> "Зразумела"
            "de" -> "Verstanden"
            "es" -> "Entendido"
            "fr" -> "Compris"
            "it" -> "Capito"
            "pt" -> "Entendido"
            "da" -> "Forstået"
            "fi" -> "Selvä"
            "kk" -> "Түсінікті"
            "lt" -> "Supratau"
            "lv" -> "Skaidrs"
            "sv" -> "Förstått"
            else -> "Got it"
        }
    }
}"""

content = content.replace("}", functions_to_add)

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)
