package com.example

object TestTabLocalizations {

    
    fun getHelpButtonDesc(lang: String): String {
        return when (lang) {
            "ru" -> "Справка"
            "uk" -> "Довідка"
            "be" -> "Даведка"
            "de" -> "Hilfe"
            "es" -> "Ayuda"
            "fr" -> "Aide"
            "it" -> "Aiuto"
            "pt" -> "Ajuda"
            "da" -> "Hjælp"
            "fi" -> "Ohje"
            "kk" -> "Анықтама"
            "lt" -> "Pagalba"
            "lv" -> "Palīdzība"
            "sv" -> "Hjälp"
            else -> "Help"
        }
    }

    fun getCloudflareTitle(lang: String): String {
        return when (lang) {
            "ru" -> "Тест скорости Cloudflare"
            "uk" -> "Тест швидкості Cloudflare"
            "be" -> "Тэст хуткасці Cloudflare"
            "de" -> "Cloudflare-Geschwindigkeitstest"
            "es" -> "Prueba de velocidad de Cloudflare"
            "fr" -> "Test de vitesse Cloudflare"
            "it" -> "Test di velocità Cloudflare"
            "pt" -> "Teste de Velocidade Cloudflare"
            "da" -> "Cloudflare hastighedstest"
            "fi" -> "Cloudflare nopeustesti"
            "kk" -> "Cloudflare жылдамдық тесті"
            "lt" -> "Cloudflare greičio testas"
            "lv" -> "Cloudflare ātruma tests"
            "sv" -> "Cloudflare hastighetstest"
            else -> "Cloudflare Speed Test"
        }
    }

    fun getCloudflareDesc(lang: String): String {
        return when (lang) {
            "ru" -> "Измерение скорости и качества через сеть Cloudflare"
            "uk" -> "Вимірювання швидкості та якості через мережу Cloudflare"
            "be" -> "Вымярэнне хуткасці і якасці праз сетку Cloudflare"
            "de" -> "Messen Sie Geschwindigkeit und Netzwerkqualität über Cloudflare CDN"
            "es" -> "Evalúe la velocidad y calidad de la red a través de Cloudflare CDN"
            "fr" -> "Mesurez la vitesse et la qualité du réseau via Cloudflare CDN"
            "it" -> "Misura velocità e qualità della rete tramite Cloudflare CDN"
            "pt" -> "Meça a velocidade e qualidade da rede via Cloudflare CDN"
            "da" -> "Mål hastighed og netværkskvalitet via Cloudflare CDN"
            "fi" -> "Mittaa nopeus ja verkon laatu Cloudflare CDN kautta"
            "kk" -> "Cloudflare CDN арқылы жылдамдық пен желі сапасын өлшеу"
            "lt" -> "Išmatuokite greitį ir tinklo kokybę per Cloudflare CDN"
            "lv" -> "Mēriet ātrumu un tīkla kvalitāti caur Cloudflare CDN"
            "sv" -> "Mät hastighet och nätverkskvalitet via Cloudflare CDN"
            else -> "Benchmark speed and network quality via Cloudflare CDN"
        }
    }

    fun getFastDesc(lang: String): String {
        return when (lang) {
            "ru" -> "Простой и быстрый тест скорости от Netflix"
            "uk" -> "Простий і швидкий тест швидкості від Netflix"
            "be" -> "Просты і хуткі тэст хуткасці ад Netflix"
            "de" -> "Einfacher und schneller Internet-Geschwindigkeitstest von Netflix"
            "es" -> "Prueba de velocidad de internet simple y rápida de Netflix"
            "fr" -> "Test de vitesse Internet simple et rapide par Netflix"
            "it" -> "Test di velocità internet semplice e rapido di Netflix"
            "pt" -> "Teste de velocidade simples e rápido da Netflix"
            "da" -> "Enkel og hurtig internet-hastighedstest af Netflix"
            "fi" -> "Yksinkertainen ja nopea Netflixin nopeustesti"
            "kk" -> "Netflix ұсынған қарапайым және жылдам жылдамдық тесті"
            "lt" -> "Paprastas ir greitas „Netflix“ interneto greičio testas"
            "lv" -> "Vienkāršs un ātrs Netflix interneta ātruma tests"
            "sv" -> "Enkelt och snabbt hastighetstest för internet från Netflix"
            else -> "Simple and fast internet speed test by Netflix"
        }
    }

    fun getWaveformDesc(lang: String): String {
        return when (lang) {
            "ru" -> "Тест задержки и стабильности пинга под нагрузкой"
            "uk" -> "Тест затримки та стабільності пінгу під навантаженням"
            "be" -> "Тэст затрымкі і стабільнасці пінгу пад нагрузкай"
            "de" -> "Latenz und Ping-Stabilität unter schwerer Netzwerklast testen"
            "es" -> "Pruebe la latencia y la estabilidad del ping bajo carga pesada"
            "fr" -> "Tester la latence et la stabilité du ping sous forte charge"
            "it" -> "Testa la latenza e la stabilità del ping sotto carico pesante"
            "pt" -> "Teste a latência e estabilidade do ping sob carga pesada"
            "da" -> "Test latenstid og ping-stabilitet under tung netværksbelantning"
            "fi" -> "Testaa latenssia ja pingin vakautta raskaassa kuormituksessa"
            "kk" -> "Ауыр жүктеме кезіндегі кідіріс пен пинг тұрақтылығын тексеру"
            "lt" -> "Išbandykite delsą ir ping stabilumą esant didelei apkrovai"
            "lv" -> "Testējiet aizkavi un ping stabilitāti pie lielas slodzes"
            "sv" -> "Testa latens och ping-stabilitet under tung nätverksbelastning"
            else -> "Test latency and ping stability under heavy network load"
        }
    }

    fun getSpeedofmeDesc(lang: String): String {
        return when (lang) {
            "ru" -> "HTML5-тест скорости без плагинов, имитирующий реальный веб-серфинг"
            "uk" -> "HTML5-тест швидкості без плагінів, що імітує реальний веб-серфінг"
            "be" -> "HTML5-тэст хуткасці без плагінаў, які імітуе рэальны вэб-сёрфінг"
            "de" -> "Plugin-freier HTML5-Geschwindigkeitstest, der echtes Surfen simuliert"
            "es" -> "Prueba de velocidad HTML5 sin complementos que simula la navegación real"
            "fr" -> "Test de vitesse HTML5 sans plug-in imitant la navigation réelle"
            "it" -> "Test di vitesse HTML5 senza plugin que simula la navigazione reale"
            "pt" -> "Teste de velocidade HTML5 sem plugins simulando navegação real"
            "da" -> "Plugin-fri HTML5 hastighedstest, der simulerer rigtig web-surfing"
            "fi" -> "Lisäosaton HTML5-nopeustesti, joka simuloi todellista selaamista"
            "kk" -> "Нақты веб-серфингті имитациялайтын плагинсіз HTML5 тесті"
            "lt" -> "Greičio testas be įskiepių, paleistas HTML5 pagrindu"
            "lv" -> "HTML5 ātruma tests bez spraudņiem, kas simulē reālu pārlūkošanu"
            "sv" -> "Plugin-fritt HTML5-hastighetstest som simulerar verklig webbsurfning"
            else -> "HTML5 speed test simulating real-world web browsing behavior"
        }
    }

    fun getWifiAnalyzerTitle(lang: String): String {
        return when (lang) {
            "ru" -> "Wi-Fi Анализатор"
            "uk" -> "Wi-Fi Аналізатор"
            "be" -> "Wi-Fi Аналізатар"
            "de" -> "Wi-Fi Analysator"
            "es" -> "Analizador Wi-Fi"
            "fr" -> "Analyseur Wi-Fi"
            "it" -> "Analizzatore Wi-Fi"
            "pt" -> "Analisador Wi-Fi"
            "da" -> "Wi-Fi Analysator"
            "fi" -> "Wi-Fi-analysaattori"
            "kk" -> "Wi-Fi Анализаторы"
            "lt" -> "Wi-Fi analizatorius"
            "lv" -> "Wi-Fi analizators"
            "sv" -> "Wi-Fi-analysator"
            else -> "Wi-Fi Analyzer"
        }
    }

    fun getWifiAnalyzerDesc(lang: String): String {
        return when (lang) {
            "ru" -> "Отображение мощности сигналов Wi-Fi сетей и распределение по каналам в реальном времени"
            "uk" -> "Відображення потужності сигналів Wi-Fi мереж та розподіл по каналах у реальному часі"
            "be" -> "Адлюстраванне магутнасці сігналаў Wi-Fi сетак і размеркаванне па каналах у рэальным часе"
            "de" -> "Echtzeit-Anzeige der Wi-Fi-Signalstärke und Kanalkonfiguration der Netze"
            "es" -> "Visualización en tiempo real de la intensidad de la señal y distribución de canales de Wi-Fi"
            "fr" -> "Visualisation en temps réel de la puissance des signaux Wi-Fi et de la distribution des canaux"
            "it" -> "Visualizzazione in tempo real dell'intensità del segnale Wi-Fi e della distribuzione dei canali"
            "pt" -> "Exibição em tempo real da força do sinal Wi-Fi e distribuição de canais das redes"
            "da" -> "Visning af Wi-Fi-signalstyrke og kanalfordeling i realtid"
            "fi" -> "Wi-Fi-verkkojen signaalin voimakkuuden ja kanavajakauman reaaliaikainen näyttö"
            "kk" -> "Нақты уақытта Wi-Fi желілерінің сигнал қуаты мен арналарын көрсету"
            "lt" -> "Realaus laiko „Wi-Fi“ signalų stiprumo ir kanalų pasiskirstymo rodymas"
            "lv" -> "Reāllaika Wi-Fi signāla stipruma un kanālu sadalījuma attēlošana"
            "sv" -> "Realtidsvisning av Wi-Fi-signalstyrka och kanalfordeling för nätverk"
            else -> "Real-time visualization of Wi-Fi network signal strengths and channel distributions"
        }
    }

    fun getDevicesDesc(lang: String): String {
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

    fun getDialogTitle(key: String, lang: String): String {
        val normalizedLang = when (lang) {
            "ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv" -> lang
            else -> "en"
        }
        return when (key) {
            "mlab" -> when (normalizedLang) {
                "ru" -> "Тест скорости M-Lab NDT"
                "uk" -> "Тест швидкості M-Lab NDT"
                "be" -> "Тэст хуткасці M-Lab NDT"
                "de" -> "M-Lab NDT Geschwindigkeitstest"
                "es" -> "Prueba de velocidad M-Lab NDT"
                "fr" -> "Test de vitesse M-Lab NDT"
                "it" -> "Test di velocità M-Lab NDT"
                "pt" -> "Teste de velocidade M-Lab NDT"
                "da" -> "M-Lab NDT hastighedstest"
                "fi" -> "M-Lab NDT nopeustesti"
                "kk" -> "M-Lab NDT жылдамдық тесті"
                "lt" -> "M-Lab NDT greičio testas"
                "lv" -> "M-Lab NDT ātruma tests"
                "sv" -> "M-Lab NDT hastighetstest"
                else -> "M-Lab NDT Speed Test"
            }
            "cloudflare" -> when (normalizedLang) {
                "ru" -> "Тест скорости Cloudflare"
                "uk" -> "Тест швидкості Cloudflare"
                "be" -> "Тэст хуткасці Cloudflare"
                "de" -> "Cloudflare-Geschwindigkeitstest"
                "es" -> "Prueba de velocidad de Cloudflare"
                "fr" -> "Test de vitesse Cloudflare"
                "it" -> "Test di velocità Cloudflare"
                "pt" -> "Teste de velocidade Cloudflare"
                "da" -> "Cloudflare hastighedstest"
                "fi" -> "Cloudflare nopeustesti"
                "kk" -> "Cloudflare жылдамдық тесті"
                "lt" -> "Cloudflare greičio testas"
                "lv" -> "Cloudflare ātruma tests"
                "sv" -> "Cloudflare hastighetstest"
                else -> "Cloudflare Speed Test"
            }
            "fast" -> when (normalizedLang) {
                "ru" -> "Тест скорости Fast.com (Netflix)"
                "uk" -> "Тест швидкості Fast.com (Netflix)"
                "be" -> "Тэст хуткасці Fast.com (Netflix)"
                "de" -> "Fast.com (Netflix) Geschwindigkeitstest"
                "es" -> "Prueba de velocidad Fast.com (Netflix)"
                "fr" -> "Test de vitesse Fast.com (Netflix)"
                "it" -> "Test di velocità Fast.com (Netflix)"
                "pt" -> "Teste de velocidade Fast.com (Netflix)"
                "da" -> "Fast.com (Netflix) hastighedstest"
                "fi" -> "Fast.com (Netflix) nopeustesti"
                "kk" -> "Fast.com (Netflix) жылдамдық тесті"
                "lt" -> "Fast.com (Netflix) greičio testas"
                "lv" -> "Fast.com (Netflix) ātruma tests"
                "sv" -> "Fast.com (Netflix) hastighetstest"
                else -> "Fast.com (Netflix) Speed Test"
            }
            "waveform" -> when (normalizedLang) {
                "ru" -> "Тест буферизации Waveform Bufferbloat"
                "uk" -> "Тест буферизації Waveform Bufferbloat"
                "be" -> "Тэст буферызацыі Waveform Bufferbloat"
                "de" -> "Waveform Bufferbloat-Test"
                "es" -> "Prueba de bufferbloat de Waveform"
                "fr" -> "Test bufferbloat Waveform"
                "it" -> "Test di bufferbloat Waveform"
                "pt" -> "Teste de bufferbloat Waveform"
                "da" -> "Waveform Bufferbloat test"
                "fi" -> "Waveform Bufferbloat -testi"
                "kk" -> "Waveform Bufferbloat тесті"
                "lt" -> "Waveform Bufferbloat testas"
                "lv" -> "Waveform Bufferbloat tests"
                "sv" -> "Waveform Bufferbloat test"
                else -> "Waveform Bufferbloat Test"
            }
            "speedofme" -> when (normalizedLang) {
                "ru" -> "Тест скорости Speedof.me"
                "uk" -> "Тест швидкості Speedof.me"
                "be" -> "Тэст хуткасці Speedof.me"
                "de" -> "Speedof.me Geschwindigkeitstest"
                "es" -> "Prueba de velocidad Speedof.me"
                "fr" -> "Test de vitesse Speedof.me"
                "it" -> "Test di velocità Speedof.me"
                "pt" -> "Teste de velocidade Speedof.me"
                "da" -> "Speedof.me hastighedstest"
                "fi" -> "Speedof.me nopeustesti"
                "kk" -> "Speedof.me жылдамдық тесті"
                "lt" -> "Speedof.me greičio testas"
                "lv" -> "Speedof.me ātruma tests"
                "sv" -> "Speedof.me hastighetstest"
                else -> "Speedof.me Speed Test"
            }
            "iperf" -> when (normalizedLang) {
                "ru" -> "Тест пропускной способности iPerf3"
                "uk" -> "Тест пропускної здатності iPerf3"
                "be" -> "Тэст прапускной здольнасці iPerf3"
                "de" -> "iPerf3-Durchsatztest"
                "es" -> "Prueba de rendimiento iPerf3"
                "fr" -> "Test de débit iPerf3"
                "it" -> "Test di throughput iPerf3"
                "pt" -> "Teste de throughput iPerf3"
                "da" -> "iPerf3 gennemstrømningstest"
                "fi" -> "iPerf3-tiedonsiirtotesti"
                "kk" -> "iPerf3 өткізу қабілетінің тесті"
                "lt" -> "iPerf3 pralaidumo testas"
                "lv" -> "iPerf3 caurlaidības tests"
                "sv" -> "iPerf3 genomströmningstest"
                else -> "iPerf3 Throughput Test"
            }
                        "devices" -> when (normalizedLang) {
                "ru" -> "Устройства, подключенные к роутеру"
                "uk" -> "Пристрої, підключені до маршрутизатора"
                "be" -> "Прылады, падлучаныя да маршрутызатара"
                "de" -> "Mit dem Router verbundene Geräte"
                "es" -> "Dispositivos conectados al enrutador"
                "fr" -> "Appareils connectés au routeur"
                "it" -> "Dispositivi connessi al router"
                "pt" -> "Dispositivos conectados ao roteador"
                "da" -> "Enheder tilsluttet routeren"
                "fi" -> "Reitittimeen yhdistetyt laitteet"
                "kk" -> "Маршрутизаторға қосылған құрылғылар"
                "lt" -> "Prie maršrutizatoriaus prijungti įrenginiai"
                "lv" -> "Maršrutētājam pievienotās ierīces"
                "sv" -> "Enheter anslutna till routern"
                else -> "Devices connected to the router"
            }

            "wifi_analyzer" -> when (normalizedLang) {
                "ru" -> "Wi-Fi Анализатор"
                "uk" -> "Wi-Fi Аналізатор"
                "be" -> "Wi-Fi Аналізатар"
                "de" -> "Wi-Fi Analysator"
                "es" -> "Analizador Wi-Fi"
                "fr" -> "Analyseur Wi-Fi"
                "it" -> "Analizzatore Wi-Fi"
                "pt" -> "Analisador Wi-Fi"
                "da" -> "Wi-Fi Analysator"
                "fi" -> "Wi-Fi-analysaattori"
                "kk" -> "Wi-Fi Анализаторы"
                "lt" -> "Wi-Fi analizatorius"
                "lv" -> "Wi-Fi analizators"
                "sv" -> "Wi-Fi-analysator"
                else -> "Wi-Fi Analyzer"
            }
            "devices" -> when (normalizedLang) {
                "ru" -> "**Список устройств:**\nЭтот инструмент отображает список всех устройств, подключенных к вашему роутеру. Вы можете видеть их IP и MAC адреса, тип подключения (Wi-Fi или Ethernet), а также статистику потребляемого трафика и текущую скорость.\n\n**Важное замечание про Ethernet:**\nДля корректного определения устройств, подключенных по кабелю, и отображения достоверной информации об их трафике, на роутере должны быть установлены специальные утилиты:\n• Если роутер имеет архитектуру с мостом (bridge), необходима утилита **bridge**.\n• Если используется архитектура со свитчом (switch), необходима утилита **swconfig**.\nБез этих утилит информация о трафике проводных устройств может отображаться некорректно (например, может выводиться общий трафик всего физического порта роутера, а не отдельного устройства)."
                else -> "**Device List:**\nThis tool displays a list of all devices connected to your router. You can see their IP and MAC addresses, connection type (Wi-Fi or Ethernet), as well as traffic consumption statistics and current speed.\n\n**Important note about Ethernet:**\nTo correctly detect wired devices and display accurate traffic information, special utilities must be installed on the router:\n• For a bridge architecture, the **bridge** utility is required.\n• For a switch architecture, the **swconfig** utility is required.\nWithout these utilities, traffic information for wired devices may be incorrect (e.g., showing total traffic for the router's physical port rather than the specific device)."
            }
            else -> ""
        }
    }

    fun getDialogBody(key: String, lang: String, config: com.example.data.RouterConfig? = null): String {
        val normalizedLang = when (lang) {
            "ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv" -> lang
            else -> "en"
        }

        return when (key) {
            "devices" -> {
                val archStr = when (config?.capabilities?.switchArchitecture) {
                    com.example.data.SwitchArchitecture.DSA -> when (normalizedLang) {
                        "ru" -> "архитектура моста (DSA)"
                        "uk" -> "архітектура мосту (DSA)"
                        "be" -> "архітэктура моста (DSA)"
                        "de" -> "Bridge-Architektur (DSA)"
                        "es" -> "arquitectura de puente (DSA)"
                        "fr" -> "architecture de pont (DSA)"
                        "it" -> "architettura bridge (DSA)"
                        "pt" -> "arquitetura de ponte (DSA)"
                        "da" -> "bridge-arkitektur (DSA)"
                        "fi" -> "silta-arkkitehtuuri (DSA)"
                        "kk" -> "көпір архитектурасы (DSA)"
                        "lt" -> "tilto architektūra (DSA)"
                        "lv" -> "tilta arhitektūra (DSA)"
                        "sv" -> "bryggarkitektur (DSA)"
                        else -> "bridge architecture (DSA)"
                    }
                    com.example.data.SwitchArchitecture.SWCONFIG -> when (normalizedLang) {
                        "ru" -> "архитектура со свитчом (SWCONFIG)"
                        "uk" -> "архітектура з комутатором (SWCONFIG)"
                        "be" -> "архітэктура з камутатарам (SWCONFIG)"
                        "de" -> "Switch-Architektur (SWCONFIG)"
                        "es" -> "arquitectura con switch (SWCONFIG)"
                        "fr" -> "architecture avec switch (SWCONFIG)"
                        "it" -> "architettura con switch (SWCONFIG)"
                        "pt" -> "arquitetura com switch (SWCONFIG)"
                        "da" -> "switch-arkitektur (SWCONFIG)"
                        "fi" -> "kytkinarkkitehtuuri (SWCONFIG)"
                        "kk" -> "коммутаторы бар архитектура (SWCONFIG)"
                        "lt" -> "komutatoriaus architektūra (SWCONFIG)"
                        "lv" -> "komutatora arhitektūra (SWCONFIG)"
                        "sv" -> "switch-arkitektur (SWCONFIG)"
                        else -> "switch architecture (SWCONFIG)"
                    }
                    com.example.data.SwitchArchitecture.UNSUPPORTED -> when (normalizedLang) {
                        "ru" -> "неподдерживаемая архитектура"
                        "uk" -> "непідтримувана архітектура"
                        "be" -> "непадтрымоўваная архітэктура"
                        "de" -> "nicht unterstützte Architektur"
                        "es" -> "arquitectura no compatible"
                        "fr" -> "architecture non prise en charge"
                        "it" -> "architettura non supportata"
                        "pt" -> "arquitetura não suportada"
                        "da" -> "ikke-understøttet arkitektur"
                        "fi" -> "ei-tuettu arkkitehtuuri"
                        "kk" -> "қолдау көрсетілмейтін архитектура"
                        "lt" -> "nepalaikoma architektūra"
                        "lv" -> "neatbalstīta arhitektūra"
                        "sv" -> "arkitektur som inte stöds"
                        else -> "unsupported architecture"
                    }
                    else -> when (normalizedLang) {
                        "ru" -> "неизвестная архитектура"
                        "uk" -> "невідома архітектура"
                        "be" -> "невядомая архітэктура"
                        "de" -> "unbekannte Architektur"
                        "es" -> "arquitectura desconocida"
                        "fr" -> "architecture inconnue"
                        "it" -> "architettura sconosciuta"
                        "pt" -> "arquitetura desconhecida"
                        "da" -> "ukendt arkitektur"
                        "fi" -> "tuntematon arkkitehtuuri"
                        "kk" -> "белгісіз архитектура"
                        "lt" -> "nežinoma architektūra"
                        "lv" -> "nezināma arhitektūra"
                        "sv" -> "okänd arkitektur"
                        else -> "unknown architecture"
                    }
                }
                
                val reqUtil = when (config?.capabilities?.switchArchitecture) {
                    com.example.data.SwitchArchitecture.DSA -> "ip-bridge"
                    com.example.data.SwitchArchitecture.SWCONFIG -> "swconfig"
                    else -> when (normalizedLang) {
                        "ru" -> "ip-bridge или swconfig"
                        "uk" -> "ip-bridge або swconfig"
                        "be" -> "ip-bridge або swconfig"
                        "de" -> "ip-bridge oder swconfig"
                        "es" -> "ip-bridge o swconfig"
                        "fr" -> "ip-bridge ou swconfig"
                        "it" -> "ip-bridge o swconfig"
                        "pt" -> "ip-bridge ou swconfig"
                        "da" -> "ip-bridge eller swconfig"
                        "fi" -> "ip-bridge tai swconfig"
                        "kk" -> "ip-bridge немесе swconfig"
                        "lt" -> "ip-bridge arba swconfig"
                        "lv" -> "ip-bridge vai swconfig"
                        "sv" -> "ip-bridge eller swconfig"
                        else -> "ip-bridge or swconfig"
                    }
                }
                
                val hasUtil = when (config?.capabilities?.switchArchitecture) {
                    com.example.data.SwitchArchitecture.DSA -> config?.capabilities?.hasBridgeUtil == true
                    com.example.data.SwitchArchitecture.SWCONFIG -> config?.capabilities?.hasSwconfigUtil == true
                    else -> false
                }
                
                val utilStatus = if (hasUtil) {
                    when (normalizedLang) {
                        "ru" -> "\nУтилита **$reqUtil** установлена в системе, всё работает корректно."
                        "uk" -> "\nУтиліта **$reqUtil** встановлена в системі, все працює коректно."
                        "be" -> "\nУтыліта **$reqUtil** усталявана ў сістэме, усё працуе карэктна."
                        "de" -> "\nDas Dienstprogramm **$reqUtil** ist im System installiert, alles funktioniert ordnungsgemäß."
                        "es" -> "\nLa utilidad **$reqUtil** está instalada en el sistema, todo funciona correctamente."
                        "fr" -> "\nL'utilitaire **$reqUtil** est installé dans le système, tout fonctionne correctement."
                        "it" -> "\nL'utilità **$reqUtil** è installata nel sistema, tutto funziona correttamente."
                        "pt" -> "\nO utilitário **$reqUtil** está instalado no sistema, tudo funciona corretamente."
                        "da" -> "\nHjælpeprogrammet **$reqUtil** er installeret i systemet, alt fungerer korrekt."
                        "fi" -> "\nTyökalu **$reqUtil** on asennettu järjestelmään, kaikki toimii oikein."
                        "kk" -> "\n**$reqUtil** утилитасы жүйеде орнатылған, барлығы дұрыс жұмыс істейді."
                        "lt" -> "\n**$reqUtil** įrankis įdiegtas sistemoje, viskas veikia tinkamai."
                        "lv" -> "\n**$reqUtil** utilītprogramma ir instalēta sistēmā, viss darbojas pareizi."
                        "sv" -> "\nVerktyget **$reqUtil** är installerat i systemet, allt fungerar korrekt."
                        else -> "\nThe **$reqUtil** utility is installed in the system, everything works correctly."
                    }
                } else {
                    when (normalizedLang) {
                        "ru" -> "\nУтилита **$reqUtil** не найдена в системе! Информация о трафике проводных устройств может отображаться некорректно."
                        "uk" -> "\nУтиліта **$reqUtil** не знайдена в системі! Інформація про трафік дротових пристроїв може відображатися некоректно."
                        "be" -> "\nУтыліта **$reqUtil** не знойдзена ў сістэме! Інфармацыя пра трафік правадных прылад можа адлюстроўвацца некарэктна."
                        "de" -> "\nDas Dienstprogramm **$reqUtil** wurde nicht im System gefunden! Verkehrsinformationen für kabelgebundene Geräte werden möglicherweise falsch angezeigt."
                        "es" -> "\n¡La utilidad **$reqUtil** no se encuentra en el sistema! La información de tráfico para dispositivos con cable puede mostrarse incorrectamente."
                        "fr" -> "\nL'utilitaire **$reqUtil** est introuvable dans le système ! Les informations de trafic pour les appareils filaires peuvent s'afficher de manière incorrecte."
                        "it" -> "\nL'utilità **$reqUtil** non è stata trovata nel sistema! Le informazioni sul traffico per i dispositivi cablati potrebbero essere visualizzate in modo errato."
                        "pt" -> "\nO utilitário **$reqUtil** não foi encontrado no sistema! As informações de tráfego para dispositivos com fio podem ser exibidas incorretamente."
                        "da" -> "\nHjælpeprogrammet **$reqUtil** findes ikke i systemet! Trafikoplysninger for kablede enheder kan vises forkert."
                        "fi" -> "\nTyökalua **$reqUtil** ei löydy järjestelmästä! Langallisten laitteiden liikennetiedot saattavat näkyä väärin."
                        "kk" -> "\n**$reqUtil** утилитасы жүйеде табылмады! Сымды құрылғылардың трафик туралы ақпараты қате көрсетілуі мүмкін."
                        "lt" -> "\nSistemoje nerastas **$reqUtil** įrankis! Informacija apie laidinių įrenginių srautą gali būti rodoma neteisingai."
                        "lv" -> "\nSistēmā nav atrasta utilītprogramma **$reqUtil**! Informācija par vārtejās ierīču datplūsmu var tikt parādīta nepareizi."
                        "sv" -> "\nVerktyget **$reqUtil** hittades inte i systemet! Trafikinformation för trådbundna enheter kan visas felaktigt."
                        else -> "\nThe **$reqUtil** utility is not found in the system! Traffic information for wired devices may be incorrect."
                    }
                }
                
                val installInstruction = when (normalizedLang) {
                    "ru" -> "\n\nДля установки необходимой утилиты скопируйте эту команду и вставьте её в консоль:\n\nopkg update && opkg install $reqUtil"
                    "uk" -> "\n\nЩоб встановити необхідну утиліту, скопіюйте цю команду та вставте її в консоль:\n\nopkg update && opkg install $reqUtil"
                    "be" -> "\n\nКаб усталяваць неабходную ўтыліту, скапіруйце гэту каманду і ўстаўце яе ў кансоль:\n\nopkg update && opkg install $reqUtil"
                    "de" -> "\n\nUm das erforderliche Dienstprogramm zu installieren, kopieren Sie diesen Befehl und fügen Sie ihn in die Konsole ein:\n\nopkg update && opkg install $reqUtil"
                    "es" -> "\n\nPara instalar la utilidad necesaria, copie este comando y péguelo en la consola:\n\nopkg update && opkg install $reqUtil"
                    "fr" -> "\n\nPour installer l'utilitaire nécessaire, copiez cette commande et collez-la dans la console :\n\nopkg update && opkg install $reqUtil"
                    "it" -> "\n\nPer installare l'utilità necessaria, copia questo comando e incollalo nella console:\n\nopkg update && opkg install $reqUtil"
                    "pt" -> "\n\nPara instalar o utilitário necessário, copie este comando e cole-o no console:\n\nopkg update && opkg install $reqUtil"
                    "da" -> "\n\nFor at installere det nødvendige hjælpeprogram, kopier denne kommando og indsæt den i konsollen:\n\nopkg update && opkg install $reqUtil"
                    "fi" -> "\n\nAsenna tarvittava työkalu kopioimalla tämä komento ja liittämällä se konsoliin:\n\nopkg update && opkg install $reqUtil"
                    "kk" -> "\n\nҚажетті утилитаны орнату үшін осы пәрменді көшіріп, консольге қойыңыз:\n\nopkg update && opkg install $reqUtil"
                    "lt" -> "\n\nNorėdami įdiegti reikiamą įrankį, nukopijuokite šią komandą ir įklijuokite ją į konsolę:\n\nopkg update && opkg install $reqUtil"
                    "lv" -> "\n\nLai instalētu nepieciešamo utilītprogrammu, kopējiet šo komandu un ielīmējiet to konsolē:\n\nopkg update && opkg install $reqUtil"
                    "sv" -> "\n\nFör att installera det nödvändiga verktyget, kopiera detta kommando och klistra in det i konsolen:\n\nopkg update && opkg install $reqUtil"
                    else -> "\n\nTo install the necessary utility, copy this command and paste it into the console:\n\nopkg update && opkg install $reqUtil"
                }
                
                val installText = if (!hasUtil && config?.capabilities?.switchArchitecture != com.example.data.SwitchArchitecture.UNSUPPORTED && config?.capabilities?.switchArchitecture != null) {
                    installInstruction
                } else ""
                
                val ethernetNote = when (normalizedLang) {
                    "ru" -> "**Важное замечание про Ethernet:**\nВаш роутер имеет: $archStr. Для возможности определения устройств, подключенных к роутеру через Ethernet, и вывода достоверной информации об их трафике необходима утилита **$reqUtil**.$utilStatus$installText"
                    "uk" -> "**Важливе зауваження щодо Ethernet:**\nВаш маршрутизатор має: $archStr. Для можливості визначення пристроїв, підключених до маршрутизатора через Ethernet, та виведення достовірної інформації про їх трафік необхідна утиліта **$reqUtil**.$utilStatus$installText"
                    "be" -> "**Важная заўвага пра Ethernet:**\nВаш маршрутызатар мае: $archStr. Для магчымасці вызначэння прылад, падлучаных да маршрутызатара праз Ethernet, і вываду дакладнай інфармацыі пра іх трафік неабходна ўтыліта **$reqUtil**.$utilStatus$installText"
                    "de" -> "**Wichtiger Hinweis zu Ethernet:**\nIhr Router verfügt über: $archStr. Um Geräte zu erkennen, die über Ethernet mit dem Router verbunden sind, und genaue Verkehrsinformationen anzuzeigen, ist das Dienstprogramm **$reqUtil** erforderlich.$utilStatus$installText"
                    "es" -> "**Nota importante sobre Ethernet:**\nSu enrutador tiene: $archStr. Para detectar dispositivos conectados al enrutador a través de Ethernet y mostrar información precisa sobre el tráfico, se requiere la utilidad **$reqUtil**.$utilStatus$installText"
                    "fr" -> "**Remarque importante concernant Ethernet :**\nVotre routeur possède : $archStr. Pour détecter les appareils connectés au routeur via Ethernet et afficher des informations de trafic précises, l'utilitaire **$reqUtil** est nécessaire.$utilStatus$installText"
                    "it" -> "**Nota importante su Ethernet:**\nIl tuo router ha: $archStr. Per rilevare i dispositivi collegati al router tramite Ethernet e visualizzare informazioni accurate sul traffico, è necessaria l'utilità **$reqUtil**.$utilStatus$installText"
                    "pt" -> "**Nota importante sobre Ethernet:**\nSeu roteador tem: $archStr. Para detectar dispositivos conectados ao roteador via Ethernet e exibir informações de tráfego precisas, o utilitário **$reqUtil** é necessário.$utilStatus$installText"
                    "da" -> "**Vigtig bemærkning om Ethernet:**\nDin router har: $archStr. For at opdage enheder forbundet til routeren via Ethernet og vise nøjagtige trafikoplysninger, kræves hjælpeprogrammet **$reqUtil**.$utilStatus$installText"
                    "fi" -> "**Tärkeä huomautus Ethernetistä:**\nReitittimessäsi on: $archStr. Jotta Ethernetin kautta reitittimeen yhdistetyt laitteet voidaan havaita ja tarkat liikennetiedot näyttää, tarvitaan työkalu **$reqUtil**.$utilStatus$installText"
                    "kk" -> "**Ethernet туралы маңызды ескертпе:**\nСіздің маршрутизаторыңызда: $archStr бар. Маршрутизаторға Ethernet арқылы қосылған құрылғыларды анықтау және олардың трафигі туралы нақты ақпаратты көрсету үшін **$reqUtil** утилитасы қажет.$utilStatus$installText"
                    "lt" -> "**Svarbi pastaba apie Ethernet:**\nJūsų maršrutizatorius turi: $archStr. Norint nustatyti įrenginius, prijungtus prie maršrutizatoriaus per Ethernet, ir rodyti tikslią informaciją apie jų srautą, reikalingas **$reqUtil** įrankis.$utilStatus$installText"
                    "lv" -> "**Svarīga piezīme par Ethernet:**\nJūsu maršrutētājam ir: $archStr. Lai noteiktu ierīces, kas pievienotas maršrutētājam, izmantojot Ethernet, un parādītu precīzu trafika informāciju, ir nepieciešama utilītprogramma **$reqUtil**.$utilStatus$installText"
                    "sv" -> "**Viktig information om Ethernet:**\nDin router har: $archStr. För att upptäcka enheter anslutna till routern via Ethernet och visa korrekt trafikinformation krävs verktyget **$reqUtil**.$utilStatus$installText"
                    else -> "**Important note about Ethernet:**\nYour router has: $archStr. To detect devices connected to the router via Ethernet and display accurate traffic information, the **$reqUtil** utility is required.$utilStatus$installText"
                }
                
                when (normalizedLang) {
                    "ru" -> "**Список устройств:**\nЭтот инструмент отображает список всех устройств, подключенных к вашему роутеру. Вы можете видеть их IP и MAC адреса, тип подключения (Wi-Fi или Ethernet), а также статистику потребляемого трафика и текущую скорость.\n\n$ethernetNote\n\nСписок устройств также можно отобразить нажав на \"Загрузка\" и \"Выгрузка\" на графике трафика и загрузки процессора на главном экране приложения."
                    "uk" -> "**Список пристроїв:**\nЦей інструмент відображає список усіх пристроїв, підключених до вашого маршрутизатора. Ви можете бачити їх IP- та MAC-адреси, тип підключення (Wi-Fi або Ethernet), а також статистику спожитого трафіку та поточну швидкість.\n\n$ethernetNote\n\nСписок пристроїв також можна відобразити натиснувши на \"Завантаження\" та \"Віддача\" на графіку трафіку та завантаження процесора на головному екрані програми."
                    "be" -> "**Спіс прылад:**\nГэты інструмент адлюстроўвае спіс усіх прылад, падлучаных да вашага маршрутызатара. Вы можаце бачыць іх IP і MAC адрасы, тып падключэння (Wi-Fi або Ethernet), а таксама статыстыку спажыванага трафіку і бягучую хуткасць.\n\n$ethernetNote\n\nСпіс прылад таксама можна адлюстраваць націснуўшы на \"Загрузка\" і \"Выгрузка\" на графіку трафіку і загрузкі працэсара на галоўным экране праграмы."
                    "de" -> "**Geräteliste:**\nDieses Tool zeigt eine Liste aller mit Ihrem Router verbundenen Geräte an. Sie können deren IP- und MAC-Adressen, Verbindungstyp (Wi-Fi oder Ethernet) sowie Verkehrsnutzungsstatistiken und die aktuelle Geschwindigkeit sehen.\n\n$ethernetNote\n\nDie Geräteliste kann auch durch Klicken auf \"Download\" und \"Upload\" im Diagramm für Datenverkehr und CPU-Auslastung auf dem Hauptbildschirm der App angezeigt werden."
                    "es" -> "**Lista de dispositivos:**\nEsta herramienta muestra una lista de todos los dispositivos conectados a su enrutador. Puede ver sus direcciones IP y MAC, el tipo de conexión (Wi-Fi o Ethernet), así como las estadísticas de consumo de tráfico y la velocidad actual.\n\n$ethernetNote\n\nLa lista de dispositivos también se puede mostrar haciendo clic en \"Descarga\" y \"Carga\" en el gráfico de tráfico y uso de CPU en la pantalla principal de la aplicación."
                    "fr" -> "**Liste des appareils :**\nCet outil affiche une liste de tous les appareils connectés à votre routeur. Vous pouvez voir leurs adresses IP et MAC, le type de connexion (Wi-Fi ou Ethernet), ainsi que les statistiques de consommation de trafic et la vitesse actuelle.\n\n$ethernetNote\n\nLa liste des appareils peut également être affichée en cliquant sur \"Téléchargement\" et \"Envoi\" sur le graphique du trafic et de l'utilisation du processeur sur l'écran principal de l'application."
                    "it" -> "**Elenco dispositivi:**\nQuesto strumento visualizza un elenco di tutti i dispositivi connessi al tuo router. Puoi vedere i loro indirizzi IP e MAC, il tipo di connessione (Wi-Fi o Ethernet), nonché le statistiche di consumo del traffico e la velocità attuale.\n\n$ethernetNote\n\nL'elenco dei dispositivi può essere visualizzato anche facendo clic su \"Download\" e \"Upload\" nel grafico del traffico e dell'utilizzo della CPU nella schermata principale dell'app."
                    "pt" -> "**Lista de dispositivos:**\nEsta ferramenta exibe uma lista de todos os dispositivos conectados ao seu roteador. Você pode ver seus endereços IP e MAC, o tipo de conexão (Wi-Fi ou Ethernet), bem como estatísticas de consumo de tráfego e velocidade atual.\n\n$ethernetNote\n\nA lista de dispositivos também pode ser exibida clicando em \"Download\" e \"Upload\" no gráfico de tráfego e uso da CPU na tela principal do aplicativo."
                    "da" -> "**Enhedsliste:**\nDette værktøj viser en liste over alle enheder, der er tilsluttet din router. Du kan se deres IP- og MAC-adresser, forbindelsestype (Wi-Fi eller Ethernet), samt trafikforbrugsstatistikker og den aktuelle hastighed.\n\n$ethernetNote\n\nEnhedslisten kan også vises ved at klikke på \"Download\" og \"Upload\" på grafen for trafik og CPU-forbrug på appens hovedskærm."
                    "fi" -> "**Laiteluettelo:**\nTämä työkalu näyttää luettelon kaikista reitittimeen yhdistetyistä laitteista. Näet niiden IP- ja MAC-osoitteet, yhteystyypin (Wi-Fi tai Ethernet) sekä liikenteen kulutustilastot ja nykyisen nopeuden.\n\n$ethernetNote\n\nLaiteluettelo voidaan näyttää myös napsauttamalla \"Lataus\" ja \"Lähetys\" liikenteen ja suorittimen käyttöasteen kaaviossa sovelluksen päänäytössä."
                    "kk" -> "**Құрылғылар тізімі:**\nБұл құрал сіздің маршрутизаторыңызға қосылған барлық құрылғылардың тізімін көрсетеді. Сіз олардың IP және MAC мекенжайларын, қосылым түрін (Wi-Fi немесе Ethernet), сондай-ақ тұтынылған трафик статистикасын және ағымдағы жылдамдықты көре аласыз.\n\n$ethernetNote\n\nҚұрылғылар тізімін қолданбаның негізгі экранындағы трафик және процессор жүктемесі графигіндегі \"Жүктеп алу\" және \"Жүктеп салу\" түймелерін басу арқылы да көрсетуге болады."
                    "lt" -> "**Įrenginių sąrašas:**\nŠis įrankis rodo visų prie maršrutizatoriaus prijungtų įrenginių sąrašą. Galite matyti jų IP ir MAC adresus, ryšio tipą („Wi-Fi“ ar Ethernet), taip pat srauto vartojimo statistiką ir dabartinį greitį.\n\n$ethernetNote\n\nĮrenginių sąrašą taip pat galima pamatyti pagrindiniame programos ekrane esančiame srauto ir procesoriaus apkrovos grafike paspaudus „Atsisiuntimas“ ir „Išsiuntimas“."
                    "lv" -> "**Ierīču saraksts:**\nŠis rīks parāda visu jūsu maršrutētājam pievienoto ierīču sarakstu. Jūs varat redzēt to IP un MAC adreses, savienojuma veidu (Wi-Fi vai Ethernet), kā arī trafika patēriņa statistiku un pašreizējo ātrumu.\n\n$ethernetNote\n\nIerīču sarakstu var parādīt arī, noklikšķinot uz \"Lejupielāde\" un \"Augšupielāde\" trafika un CPU lietojuma diagrammā lietotnes galvenajā ekrānā."
                    "sv" -> "**Enhetslista:**\nDetta verktyg visar en lista över alla enheter anslutna till din router. Du kan se deras IP- och MAC-adresser, anslutningstyp (Wi-Fi eller Ethernet), samt statistik över trafikförbrukning och aktuell hastighet.\n\n$ethernetNote\n\nEnhetslistan kan också visas genom att klicka på \"Nedladdning\" och \"Uppladdning\" i diagrammet för trafik och CPU-användning på appens huvudskärm."
                    else -> "**Device List:**\nThis tool displays a list of all devices connected to your router. You can see their IP and MAC addresses, connection type (Wi-Fi or Ethernet), as well as traffic consumption statistics and current speed.\n\n$ethernetNote\n\nThe device list can also be displayed by clicking on \"Download\" and \"Upload\" on the traffic and CPU usage graph on the main screen of the app."
                }
            }
            "mlab" -> when (normalizedLang) {
                "ru" -> "**Суть:** Академический эталон чистого состояния сети.\n\n• **Принцип:** Открывает одно TCP-соединение через WebSockets на 10 секунд для замера чистой скорости приложений (Goodput).\n• **Алгоритм:** Использует алгоритм Google BBR для оценки реальной емкости канала без переполнения буфера."
                "uk" -> "**Суть:** Академічний еталон чистого стану мережі.\n\n• **Принцип:** Відкриває одне TCP-з’єднання через WebSockets на 10 секунд для вимірювання чистої швидкості додатків (Goodput).\n• **Алгоритм:** Використовує алгоритм Google BBR для оцінки реальної ємності каналу без переповнення буфера."
                "be" -> "**Сутнасць:** Акадэмічны эталон чыстага стану сеткі.\n\n• **Прынцып:** Адкрывае адно TCP-злучэнне праз WebSockets на 10 секунд для вымярэння чыстай хуткасці прыкладанняў (Goodput).\n• **Алгарытм:** Выкарыстоўвае алгарытм Google BBR для ацэнкі рэальнай ёмістасці канала без перапаўнення буфера."
                "de" -> "**Konzept:** Akademischer Benchmark für sauberen Netzwerkstatus.\n\n• **Prinzip:** Öffnet eine TCP-Verbindung über WebSockets für 10 Sekunden, um den Durchsatz (Goodput) zu messen.\n• **Algorithmus:** Nutzt Google BBR für präzise Ergebnisse ohne künstliche Puffer-Füllung."
                "es" -> "**Concepto:** Punto de referencia académico del estado limpio de la red.\n\n• **Método:** Abre una sola conexión TCP por WebSockets durante 10 segundos para medir el rendimiento de la aplicación (Goodput).\n• **Algoritmo:** Utiliza Google BBR para medir la velocidad real sin saturar búferes."
                "fr" -> "**Concept :** Référence académique de l'état propre du réseau.\n\n• **Principe :** Ouvre une unique connexion TCP via WebSockets pendant 10 secondes pour mesurer le débit applicatif utile (Goodput).\n• **Algorithme :** Utilise le mécanisme Google BBR sans saturer inutilement les tampons."
                "it" -> "**Concetto:** Benchmark accademico dello stato pulito della rete.\n\n• **Metodo:** Apre una singola connessione TCP tramite WebSockets per 10 secondi per valutare la velocità applicativa (Goodput).\n• **Algoritmo:** Utilizza Google BBR per trovare la capacità reale senza intasare i buffer."
                "pt" -> "**Conceito:** Referência acadêmica de medição de rede limpa.\n\n• **Método:** Abre uma única conexão TCP via WebSockets por 10 segundos para registrar a taxa de dados útil (Goodput).\n• **Algoritmo:** Utiliza o Google BBR para obter a largura de banda útil real sem bufferbloat."
                "da" -> "**Koncept:** Akademisk benchmark for en ren netværkstilstand.\n\n• **Metode:** Åbner en enkelt TCP-forbindelse via WebSockets i 10 sekunder for at måle fart på applikationsniveau (Goodput).\n• **Algoritme:** Bruger Google BBR til at finde reel kapacitet uden ophobning i bufferen."
                "fi" -> "**Konsepti:** Akateeminen viitekehys verkon puhtaalle tilalle.\n\n• **Toiminta:** Avaa yhden TCP-yhteyden WebSockets-protokollalla 10 sekunniksi mitatakseen sovellustason nopeutta (Goodput).\n• **Algoritmi:** Käyttää Google BBR -hallintaa todellisen rajatilan löytämiseen ilman puskurointia."
                "kk" -> "**Табиғаты:** Таза желі күйін бағалаудың академиялық эталоны.\n\n• **Принципі:** Қолданба деңгейіндегі таза жылдамдықты (Goodput) өлшеу үшін WebSockets арқылы 10 секундқа бір TCP қосылымын ашады.\n• **Алгоритм:** Буферлерді артық толтырмай жылдамдықты анықтау үшін Google BBR алгоритмін қолданады."
                "lt" -> "**Esmė:** Akademinis švaraus tinklo būsenos etalonas.\n\n• **Veikimas:** Atidaro vieną TCP ryšį per saugius WebSockets 10-čiai sekundžių, kad išmatuotų realų pralaidumą (Goodput).\n• **Algoritmas:** Naudoja Google TCP BBR algoritmą spūstims valdyti neužpildant buferio."
                "lv" -> "**Būtība:** Akadēmiskais tīkla tīrā stāvokļa etalons.\n\n• **Darbība:** Atver vienu TCP savienojumu caur WebSockets uz 10 sekundēm, mērot reālo caurlaidības spēju lietojumprogrammu līmenī (Goodput).\n• **Algoritms:** Izmanto Google TCP BBR sastrēgumu algoritmu reālās jaudas noteikšanai."
                "sv" -> "**Koncept:** Akademiskt benchmark för ren nätverksstatus.\n\n• **Metod:** Öppnar en enskild TCP-anslutning via WebSockets under 10 sekunder för att mäta genomströmning på applikationsnivå (Goodput).\n• **Algoritm:** Använder Google BBR för att lokalisera kapacitet utan onödig buffertpåverkan."
                else -> "**Concept:** Academic benchmark of clean network state.\n\n• **How it works:** Opens a single TCP connection over WebSockets for 10 seconds to measure real app-level speed (Goodput).\n• **Algorithm:** Uses Google's TCP BBR to measure capacity without buffer bloat."
            }
            "cloudflare" -> when (normalizedLang) {
                "ru" -> "**Суть:** Комплексный стресс-тест качества маршрутизации и задержек.\n\n• **Принцип:** Последовательно скачивает блоки файлов (от 100 КБ до 25 МБ) для построения графиков стабильности.\n• **Джиттер:** Измеряет колебания пинга в состоянии покоя.\n• **Под нагрузкой:** Фиксирует рост пинга во время одновременной передачи тяжелого блока 25 МБ."
                "uk" -> "**Суть:** Комплексний стрес-тест якості маршрутизації та затримок.\n\n• **Принцип:** Послідовно завантажує блоки файлів (від 100 КБ до 25 МБ) для побудови графіків стабільності.\n• **Джитер:** Вимірює коливання пінгу в стані спокою.\n• **Під навантаженням:** Фіксує зростання пінгу під час одночасної передачі важкого блоку 25 МБ."
                "be" -> "**Сутнасць:** Комплексны стрэс-тэст якасці маршрутызацыі і затрымак.\n\n• **Прынцып:** Паслядоўна спампоўвае блокі файлаў (ад 100 КБ да 25 МБ) для пабудовы графікаў стабільнасці.\n• **Джытэр:** Вымярае ваганні пінгу ў стане спакою.\n• **Пад нагрузкай:** Фіксуе рост пінгу падчас адначасовай перадачы цяжкага блоку 25 МБ."
                "de" -> "**Konzept:** Umfassender Stresstest für Verbindungs- und Routingqualität.\n\n• **Prinzip:** Ruft nacheinander feste Dateiblöcke (100 KB bis 25 MB) ab, um Stabilitätskurven zu erstellen.\n• **Jitter:** Bewertet die Schwankung der Latenz im Ruhezustand.\n• **Loaded Latency:** Überprüft den Latenzanstieg während eines großen 25-MB-Dateitransfers."
                "es" -> "**Concepto:** Prueba de esfuerzo para auditoría de enrutamiento y latencia.\n\n• **Método:** Solicita secuencialmente bloques de datos (100 KB a 25 MB) para graficar consistencia estadística.\n• **Jitter:** Mide los saltos de ping en condiciones normales.\n• **Latencia con carga:** Evalúa la subida del ping mientras se descarga un bloque grande de 25 MB."
                "fr" -> "**Concept :** Test d'effort de la qualité du routage et de la latence.\n\n• **Principe :** Télécharge des fichiers de tailles fixes (100 Ko à 25 Mo) pour construire des graphiques de régularité.\n• **Jitter :** Évalue les fluctuations du ping au repos.\n• **Latence chargée :** Mesure la hausse du ping lors d'un transfert parallèle de 25 Mo."
                "it" -> "**Concetto:** Stress test per il controllo di instabilità e latenza di rete.\n\n• **Metodo:** Richiede pacchetti con dimensioni crescenti (100 KB - 25 MB) tracciando grafici di continuità.\n• **Jitter:** Calcola la stabilità temporale del ping.\n• **Latenza sotto carico:** Rileva il degrado della latenza durante il transito simultaneo di 25 MB."
                "pt" -> "**Conceito:** Teste de estresse para auditar roteamento e latência.\n\n• **Método:** Requisita blocos sequenciais (100 KB a 25 MB) para analisar a consistência estatística.\n• **Jitter:** Mede a instabilidade do ping em repouso.\n• **Latência Carregada:** Analisa o aumento do ping durante transferência ativa de 25 MB."
                "da" -> "**Koncept:** Omfattende stresstest af rute- og latenskvalitet.\n\n• **Metode:** Henter filer af faste størrelser (100 KB til 25 MB) for at bygge præcise stabilitetskurver.\n• **Jitter:** Måler udsving i latenstid under normale forhold.\n• **Latenstid under belastning:** Viser stigningen på ping under en parallel 25 MB filoverførsel."
                "fi" -> "**Konsepti:** Kattava reitityksen ja latenssin laadun stressitesti.\n\n• **Toiminta:** Lataa vaiheittaisia tiedostolohkoja (100 kt - 25 Mt) luodakseen tilastollisia suorituskykykuvia.\n• **Jitter:** Mittaa latenssin vakautta (pingin vaihtelua levössä).\n• **Loaded Latency:** Seuraa ping-arvon nousua rinnakkaisen 25 Mt kuormituksen aikana."
                "kk" -> "**Табиғаты:** Бағыттау сапасы мен кідірістерді кешенді стресс-тестілеу.\n\n• **Принципі:** Тұрақтылық графиктерін құру үшін файл блоктарын (100 КБ-тан 25 МБ-қа дейін) кезекпен жүктейді.\n• **Джиттер:** Тыныш күйдегі пингтің ауытқу деңгейін өлшейді.\n• **Жүктемедегі пинг:** Белсенді 25 МБ жүктеу кезінде пингтің қаншалықты өсетінін бақылайды."
                "lt" -> "**Esmė:** Visapusiškas streso testas ryšio kokybei ir delsai audituoti.\n\n• **Veikimas:** Siunčiasi fiksuoto dydžio blokus (100 KB – 25 MB) stabilumo grafikams sudaryti.\n• **Jitter:** Išmatuoja delsos svyravimus ramybės būsenoje.\n• **Apkrauta delsa:** Fiksuoja delsos padidėjimą vienu metu siunčiant 25 MB duomenų bloką."
                "lv" -> "**Būtība:** Visaptverošs stresa tests savienojuma un maršrutēšanas kvalitātes noteikšanai.\n\n• **Darbība:** Secīgi pieprasa fiksētus datu blokus (100 KB līdz 25 MB) stabilitātes grafiku izveidei.\n• **Džiters:** Aprēķina ping svārstības miera stāvoklī.\n• **Loaded Latency:** Fiksē ping pieaugumu pie paralēlas 25 MB lejupielādes."
                "sv" -> "**Koncept:** Omfattende stresstest för kvalitetssäkring av routing och latens.\n\n• **Metod:** Begär fasta filblock (100 KB till 25 MB) i följd för statistisk utvärdering.\n• **Jitter:** Mäter ping-variationer under tomgång.\n• **Loaded Latency:** Visar ökningen i ping under överföring av den tunga 25 MB filen."
                else -> "**Concept:** Detailed stress-test for routing quality and latency auditing.\n\n• **How it works:** Sequentially requests fixed-size blocks (100 KB to 25 MB) to build percentile stability graphs.\n• **Jitter:** Evaluates how much ping bounces under resting conditions.\n• **Loaded Latency:** Tracks how much ping increases during parallel 25 MB file transfers."
            }
            "fast" -> when (normalizedLang) {
                "ru" -> "**Суть:** Бытовой потребительский тест «последней мили» от Netflix.\n\n• **Принцип:** Скачивает данные напрямую с серверов Netflix Open Connect. Поскольку провайдеры не могут занизить скорость к Fast.com без ухудшения работы самого Netflix, тест показывает максимально честные результаты.\n• **Цель:** Помогает понять, почему зависают фильмы под нагрузкой."
                "uk" -> "**Суть:** Споживчий тест «останньої милі» від Netflix.\n\n• **Принцип:** Завантажує дані безпосередньо з серверів Netflix Open Connect. Оскільки провайдери не можуть занизити швидкість до Fast.com без погіршення роботи самого Netflix, тест показує максимально чесні показники.\n• **Мета:** Допомагає зрозуміти, чому зависають фільми під навантаженням."
                "be" -> "**Сутнасць:** Спажывецкі тэст «апошняй мілі» ад Netflix.\n\n• **Прынцып:** Спампоўваў даныя наўпрост з сервераў Netflix Open Connect. Паколькі правайдэры не могуць занізіць хуткасць да Fast.com без пагаршэння працы самога Netflix, тэст паказвае максімальна сумленныя вынікі.\n• **Мэта:** Дапамагае зразумець, чаму завісаюць фільмы пад нагрузкай."
                "de" -> "**Konzept:** Streaming-fokussierter Verbrauchertest von Netflix auf der letzten Meile.\n\n• **Prinzip:** Lädt Inhalte direkt von den Open Connect Netflix-CDNs herunter. Anbieter können Fast.com nicht drosseln, ohne das Streaming der echten Nutzer zu verschlechtern, was zu ehrlichen Messungen zwingt.\n• **Nutzen:** Prüft, ob Video-Ruckler an der Verbindungsrate liegen."
                "es" -> "**Concepto:** Prueba de velocidad práctica enfocada en streaming de Netflix.\n\n• **Método:** Descarga multimedia directo desde los servidores Open Connect de Netflix. Al simular tráfico real, impide que los proveedores alteren o prioricen artificialmente la tasa de datos.\n• **Uso:** Confirma si los problemas en series se deben a la conexión del proveedor."
                "fr" -> "**Concept :** Test client pragmatique axé sur le streaming vidéo par Netflix.\n\n• **Principe :** Télécharge du contenu depuis les serveurs CDN Netflix Open Connect. Les FAI ne pouvant pas brider ce flux sans nuire directement à l'utilisation de Netflix, les débits affichés sont réels.\n• **But :** Détermine si les problèmes de lecture vidéo dépendent de votre opérateur."
                "it" -> "**Concetto:** Test di velocità pratico mirato allo streaming video di Netflix.\n\n• **Metodo:** Scarica dati direttamente dai server della rete Open Connect di Netflix. Simulando traffico reale, previene priorità fittizie (QoS) applicate dai gestori.\n• **Scopo:** Utile per capire se i rallentamenti dei film dipendono dalla linea."
                "pt" -> "**Conceito:** Teste prático de banda útil residencial mantido pela Netflix.\n\n• **Método:** Puxa vídeos das redes CDN do Netflix Open Connect. Por simular tráfego doméstico comum, evita otimizações fajutas (QoS) de provedores.\n• **Uso:** Excelente para saber se os travamentos em vídeos ocorrem por ação da operadora."
                "da" -> "**Koncept:** Forbrugerorienteret hastighedstest til streaming från Netflix.\n\n• **Metode:** Henter mediefiler direkte fra Netflix' egne Open Connect servere. Udbydere kan ikke drosle signalet uden at ødelægge selve Netflix, hvilket tvinger dem til at vise reel hastighed.\n• **Formål:** God til at afsløre, om dine film hakker pga. internettet."
                "fi" -> "**Konsepti:** Suoratoistoon keskittyvä käytännön kuluttajatesti Netflixiltä.\n\n• **Toiminta:** Lataa sisältöä suoraan Netflix Open Connect -palvelimista. Operaattorit eivät voi rajoittaa Fast.comia ilman, että Netflixin laatu heikkenee, mikä paljastaa todellisen nopeuden.\n• **Käyttö:** Helppo tapa selvittää, johtuuko elokuvien puskurointi verkkoyhteydestä."
                "kk" -> "**Табиғаты:** Медиа контентке арналған Netflix тұтынушылық тесті.\n\n• **Принципі:** Файлдарды тікелей Netflix Open Connect серверлерінен жүктейді. Провайдерлер ОЖ сапасын нашарлатпай Fast.com жылдамдығын шектей алмайтындықтан, сынақ өте әділ нәтиже береді.\n• **Мақсаты:** Фильмдердің неліктен кептелетінін анықтауға көмектеседі."
                "lt" -> "**Esmė:** Vartotojams skirtas vaizdo srauto įvertinimo testas iš Netflix.\n\n• **Veikimas:** Siunčiasi duomenis tiesiai iš Netflix Open Connect CDN serverių. Kadangi operatoriai negali riboti Fast.com nesuprastindami pačio Netflix paslaugos, gaunamas sąžiningas greitis.\n• **Tikslas:** Padeda nustatyti, ar vaizdo įrašai stringa dėl ryšio kokybės."
                "lv" -> "**Būtība:** Mājas patērētājiem piemērots Netflix ātruma tests.\n\n• **Darbība:** Nolasa datus tieši no Netflix Open Connect CDN serveriem. Tā kā sniedzēji nevar ierobežot Fast.com darbību, neupurējot pašu Netflix, tests uzrāda pilnīgi godīgu ātrumu.\n• **Mērķis:** Noskaidro, vai filmu buferēšana ir saistīta ar līniju."
                "sv" -> "**Koncept:** Konsumenttest för mediastreaming från Netflix.\n\n• **Metod:** Hämtar data direkt från Netflix Open Connect CDN-serverar. Operatörer kan inte strypa Fast.com utan att försämra Netflix, vilket garanterar en rättvisande vardagsmätning.\n• **Syfte:** Tar reda på om videobuffringen beror på din internetleverantör."
                else -> "**Concept:** Stream-focused last-mile consumer speed test by Netflix.\n\n• **How it works:** Directly fetches media data from Netflix CDNs (Open Connect). ISPs prioritize normal test traffic, but throttling Fast.com directly degrades movie streaming for users—enforcing honest reporting.\n• **Goal:** Verifies if Netflix buffering is due to ISP limitations."
            }
            "waveform" -> when (normalizedLang) {
                "ru" -> "**Суть:** Диагностика работы роутера и уязвимости к задержкам буферизации (Bufferbloat).\n\n• **О буферизации:** При сильной нагрузке роутер задерживает пакеты в памяти, из-за чего резко прыгает пинг, ломая игровой процесс и связь.\n• **Принцип:** Сравнивает задержку в покое с задержкой при забитом канале, выставляя оценку от A до F."
                "uk" -> "**Суть:** Діагностика роботи роутера та вразливості до затримок буферизації (Bufferbloat).\n\n• **Про буферизацію:** При сильному навантаженні роутер затримує пакети в пам’яті, через що різко стрибає пінг, ламаючи ігровий процес та зв’язок.\n• **Принцип:** Порівнює затримку в спокої із затримкою при забитому каналі, виставляючи оцінку від A до F."
                "be" -> "**Сутнасць:** Дыягностыка працы роўтара і ўразлівасці да затрымак буферызацыі (Bufferbloat).\n\n• **Пра буферызацыю:** Пры моцнай нагрузцы роўтар затрымлівае пакеты ў памяці, з-за чаго рэзка скача пінг, ламаючы гульнявы працэс і сувязь.\n• **Прынцып:** Параўноўвае затрымку ў спакоі з затрымкай пры забітым канале, выстаўляючы адзнаку ад A да F."
                "de" -> "**Konzept:** Diagnose der Verbindungsstabilität bei Pufferüberlauf (Bufferbloat).\n\n• **Bufferbloat:** Unter Volllast speichert der Router Pakete im internen Puffer, was zu Latenzspitzen führt und Onlinegaming oder Telefonate stört.\n• **Prinzip:** Misst die Latenz im Leerlauf und vergleicht sie mit der Latenz unter hoher Netzauslastung (Note A bis F)."
                "es" -> "**Concepto:** Evaluación del enrutador ante la saturación del búfer (Bufferbloat).\n\n• **Saturación:** Al descargar al límite, el router retiene paquetes en su memoria intermedia, disparando el ping y arruinando llamadas o juegos.\n• **Método:** Compara el ping en reposo contra el ping obtenido en descarga activa, otorgando notas de A a F."
                "fr" -> "**Concept :** Diagnostic de l'encombrement de la mémoire tampon du routeur (Bufferbloat).\n\n• **Bufferbloat :** Lors d'un téléchargement intensif, le routeur accumule les paquets dans sa RAM, ce qui fait bondir le ping et détruit la voix IP ou le jeu en ligne.\n• **Principe :** Compare le ping à vide avec le ping sous saturation de bande passante (notes de A à F)."
                "it" -> "**Concetto:** Diagnosi dei problemi legati alla congestione del buffer (Bufferbloat).\n\n• **Bufferbloat:** Quando la linea è al massimo, il router accoda i pacchetti in memoria. Ciò causa sbalzi di ping che penalizzano il gaming e le chiamate.\n• **Metodo:** Integra una misurazione del ping a riposo verificandone la stabilità sotto carico (voti da A a F)."
                "pt" -> "**Conceito:** Diagnóstico de controle do roteador sob pressão de dados (Bufferbloat).\n\n• **Bufferbloat:** Ao baixar arquivos na velocidade máxima, o roteador guarda pacotes em buffers, gerando picos de latência que travam jogos ou chamadas de voz.\n• **Método:** Registra o ping sem carga em contraste com o ping sob saturação, atribuindo notas de A a F."
                "da" -> "**Koncept:** Diagnosticering af routerens evne til at undgå bufferbelastning (Bufferbloat).\n\n• **Bufferbloat:** Under fuld belastning ophober routeren pakker i sin hukommelse, hvilket får ping til at stige voldsomt, så parallel brug af nettet bliver besværlig.\n• **Hvordan det virker:** Sammenligner tomgangs-latency med latency under tung download/upload (karakter A til F)."
                "fi" -> "**Konsepti:** Reitittimen ruuhkanhallinnan ja 'Bufferbloat'-puskurointiongelman testaus.\n\n• **Bufferbloat:** Täydellä latauksella reititin puskuroi paketteja liikaa, mikä nostaa ping-viivettä ja haittaa reaaliaikaisia pelejä sekä puheluita.\n• **Toiminta:** Vertaa viivettä levessä ja tuloksia rinnakkaisen kuormituksen aikana, antaen arvosanan A-F."
                "kk" -> "**Табиғаты:** Роутердің буферлеуді басқару деңгейін және Bufferbloat мәселелерін диагностикалау.\n\n• **Буферлеу туралы:** Желі толық жүктелгенде, роутер пакеттерді ішкі жадында сақтайды, бұл пингтің күрт өсуіне әкеліп, онлайн ойындар мен байланысты қиындатады.\n• **Принципі:** Тегіс желідегі пинг пен максималды жүктемедегі пингті салыстырып, А-дан F-ке дейін баға береді."
                "lt" -> "**Esmė:** Maršruto parinktuvo naudingumo ir imtumo (Bufferbloat) diagnostika.\n\n• **Bufferbloat:** Esant apkrovai, parinktuvas kaupia paketus vidinėje atmintyje, todėl delsos laikas staigiai šokteli, trikdydamas realaus laiko skambučius bei žaidimus.\n• **Veikimas:** Lygina delsą ramybės būsenoje su delsa, gauta apkrovus ryšį, skiriant įvertinimą nuo A iki F."
                "lv" -> "**Būtība:** Maršrutētāja nostrèsa pretestības un pakotņu buferizācijas (Bufferbloat) diagnostika.\n\n• **Bufferbloat:** Pie pilnas slodzes maršrutētājs rindos pakotnes savā RAM atmiņā, izraisot milzīgus ping lēcienus, kas traucē spēlēm un sakariem.\n• **Darbība:** Salīdzina ping mierīgā stāvoklī ar pakāpenisko ping noslodzes laikā (vērtējums no A līdz F)."
                "sv" -> "**Koncept:** Diagnos av routerns prestanda och känslighet för Bufferbloat.\n\n• **Bufferbloat:** Under tung belastning lagrar routern paket i sin buffert, vilket orsakar gigantiska ping-spikar i realtidsapplikationer (röstsamtal, spel).\n• **Metod:** Jämför tomgångs-ping med svarstid under full belastning i nedladdning (betyg A till F)."
                else -> "**Concept:** Direct diagnostics of router performance under heavy buffer load (Bufferbloat).\n\n• **Bufferbloat:** When connection is at its peak, the router stores packets in its queue memory, which heavily spikes ping.\n• **Principle:** Compares the idle latency against the loaded latency under simultaneous heavy usage to score the connection from A to F."
            }
            "iperf" -> IperfLocalizations.getDialogBody(normalizedLang)
            "speedofme" -> when (normalizedLang) {
                "ru" -> "**Суть:**\nSpeedof.me — полностью браузерный (HTML5) инструмент для измерения реальной скорости и стабильности интернет-соединения. Его главное отличие от классических тестов — отказ от Flash, Java или WebSocket в пользу имитации реального веб-серфинга путем последовательной загрузки файлов через обычные HTTP-запросы.\n\n**Принцип:**\nТест базируется на принципе «от простого к сложному» и проверяет соединение в условиях повседневной загрузки сайтов:\n• **Без искусственного разгона:** Файлы скачиваются напрямую через стандартный HTTP-протокол, не позволяя провайдерам использовать «чит-коды» (кэширование или приоритизацию трафика).\n• **Адаптивность:** Тест динамически подстраивается под мощность вашего канала, не перегружая слабые соединения и не ограничивая гигабитные линии.\n• **Чистый HTML5:** Работает без установки приложений на любых устройствах (ПК, смартфоны, Smart TV) через любой современный браузер.\n\n**Алгоритм:**\n1. **Определение задержки (Ping / Jitter):** Серия микро-запросов к ближайшему из более 100 серверов для замера стабильности пинга.\n2. **Тест загрузки (Download):** Скачивание блоков начиная от 128 КБ с увеличением размера (вплоть до 128 МБ), пока время загрузки блока не превысит порог в 8 секунд. Это замеряет скорость на старте и на пике.\n3. **Тест отдачи (Upload):** Зеркальный процесс отправки файлов по возрастающей схеме (от мелких к крупным) для фиксации стабильной скорости отдачи."
                "uk" -> "**Суть:**\nSpeedof.me — повністю браузерний (HTML5) інструмент для вимірювання реальної швидкості та стабільності інтернет-з'єднання. Його головна відмінність від класичних тестів — відмова від Flash, Java або WebSocket на користь імітації реального веб-серфінгу шляхом послідовного завантаження файлів через звичайні HTTP-запити.\n\n**Принцип:**\nТест базується на принципі «від простого до складного» і перевіряє з'єднання в умовах повсякденного завантаження сайтів:\n• **Без штучного розгону:** Файли завантажуються безпосередньо через стандартний HTTP-протокол, не дозволяючи провайдерам використовувати «чит-коди» (кешування або пріоритезацію трафіку).\n• **Адаптивність:** Тест динамічно підлаштовується під потужність вашого каналу, не перевантажуючи слабкі з'єднання і не обмежуючи гігабітні лінії.\n• **Чистий HTML5:** Працює без встановлення додатків на будь-яких пристроях (ПК, смартфони, Smart TV) через будь-який сучасний браузер.\n\n**Алгоритм:**\n1. **Визначення затримки (Ping / Jitter):** Серія мікрозапитів до найближчого з понад 100 серверів для вимірювання стабільності пінгу.\n2. **Тест завантаження (Download):** Завантаження блоків починаючи від 128 КБ зі збільшенням розміру (аж до 128 МБ), поки час завантаження блоку не перевищить поріг у 8 секунд. Це вимірює швидкість на старті та на піку.\n3. **Тест віддачі (Upload):** Дзеркальний процес надсилання файлів за зростаючою схемою (від дрібних до великих) для фіксації стабільної швидкості віддачі."
                "be" -> "**Сутнасць:**\nSpeedof.me — цалкам браўзерны (HTML5) інструмент для вымярэння рэальнай хуткасці і стабільнасці інтэрнэт-злучэння. Яго галоўнае адрозненне ад класічных тэстаў — адмова ад Flash, Java або WebSocket на карысць імітацыі рэальнага вэб-сёрфінгу шляхом паслядоўнай загрузкі файлаў праз звычайныя HTTP-запыты.\n\n**Прынцып:**\nТэст базіруецца на прынцыпе «ад простага да складанага» і правярае злучэнне ў умовах паўсядзённай загрузкі сайтаў:\n• **Без штучнага разгону:** Файлы спампоўваюцца наўпрост праз стандартны HTTP-пратакол, не дазваляючы правайдэрам выкарыстоўваць «чыт-коды» (кэшаванне або прыярытэзацыю трафіку).\n• **Адаптатыўнасць:** Тэст дынамічно падладжваецца пад магутнасць вашага канала, не перагружаючы слабыя злучэнні і не абмяжоўваючы гігабітныя лініі.\n• **Чысты HTML5:** Працуе без усталёўкі прыкладанняў на любых прыладах (ПК, смартфоны, Smart TV) праз любы сучасны браўзер.\n\n**Алгарытм:**\n1. **Вызначэнне затрымкі (Ping / Jitter):** Серыя мікрозапытаў да бліжэйшага з больш чым 100 сервераў для замеру стабільнасці пінгу.\n2. **Тэст загрузкі (Download):** Спампоўванне блокаў пачынаючы ад 128 КБ з павелічэннем памеру (аж да 128 МБ), пакуль час загрузкі блока не перавысіць парог у 8 секунд. Гэта вымярае хуткасць на старце і на піку.\n3. **Тэст аддачы (Upload):** Дзеркальны працэс адпраўкі файлаў па ўзрастаючай схеме (ад дробных да буйных) для фіксацыі стабільнай хуткасці аддач."
                "de" -> "**Konzept:**\nSpeedof.me ist ein vollständig browserbasiertes (HTML5) Tool zur Messung der tatsächlichen Geschwindigkeit und Stabilität Ihrer Internetverbindung. Der Hauptunterschied zu herkömmlichen Tests besteht darin, dass auf Flash, Java oder WebSockets verzichtet wird, um echtes Surfen durch das sequentielle Laden von Dateien über Standard-HTTP-Anfragen zu simulieren.\n\n**Prinzip:**\nDer Test basiert auf dem Prinzip „vom Einfachen zum Komplexen“ und testet die Verbindung unter realen Bedingungen:\n• **Ohne künstliche Beschleunigung:** Dateien werden direkt über HTTP heruntergeladen, sodass ISPs keine Priorisierungs-Tricks anwenden können.\n• **Anpassungsfähigkeit:** Passt sich dynamisch an die Bandbreite an, um schwache Verbindungen nicht zu überlasten und Gigabit-Leitungen voll auszunutzen.\n• **Reines HTML5:** Funktioniert ohne App-Installationen auf PCs, Smartphones oder Smart-TVs in jedem Browser.\n\n**Algorithmus:**\n1. **Latenz-Messung (Ping / Jitter):** Mikrosekundengenaue Abfragen an den nächsten von über 100 Servern.\n2. **Download-Test (Download):** Herunterladen von Blöcken ab 128 KB bis zu 128 MB, bis die Ladedauer ca. 8 Sekunden erreicht, um die Spitzengeschwindigkeit zu messen.\n3. **Upload-Test (Upload):** Spiegelbildlicher Prozess des Hochladens immer größerer Dateiblöcke zur Verifizierung der Upload-Rate."
                "es" -> "**Concepto:**\nSpeedof.me es una herramienta basada 100% en navegador (HTML5) para medir la velocidad y estabilidad reales de la conexión. Se distingue de los tests clásicos al omitir Flash, Java o WebSockets en favor de simular una navegación web real mediante descargas secuenciales de archivos a través de peticiones HTTP ordinarias.\n\n**Principios:**\n• **Sin aceleración artificial:** Los archivos se descargan directamente por HTTP estándar, evitando que los proveedores apliquen trucos de priorización o caché.\n• **Adaptabilidad:** Ajusta dinámicamente el tamaño de los archivos para no saturar conexiones débiles ni limitar artificialmente las líneas de gigabit.\n• **HTML5 puro:** Funciona en PC, móviles o Smart TV sin necesidad de instalar apps, usando cualquier navegador moderno.\n\n**Algoritmo:**\n1. **Medición de latencia (Ping / Jitter):** Envío de micro-peticiones al servidor más cercano de entre más de 100 para evaluar la estabilidad del ping.\n2. **Prueba de descarga (Download):** Descarga bloques de datos desde 128 KB hasta 128 MB, aumentando de tamaño progresivamente hasta alcanzar un umbral de 8 segundos para hallar la tasa pico.\n3. **Prueba de subida (Upload):** Proceso simétrico que envía archivos de menor a mayor tamaño para registrar la velocidad máxima estable de subida."
                "fr" -> "**Concept :**\nSpeedof.me est un outil entièrement basé sur le navigateur (HTML5) pour mesurer la vitesse et la stabilité réelles d'une connexion Internet. Sa différence majeure avec les tests classiques est l'absence de Flash, Java ou WebSockets, simulant une navigation web authentique via des téléchargements HTTP successifs.\n\n**Principe :**\nLe test repose sur une méthode progressive et analyse la connexion dans des conditions d'utilisation quotidiennes :\n• **Pas d'accélération artificielle :** Les fichiers sont téléchargés via le protocole HTTP standard, empêchant les FAI d'utiliser la mise en cache ou la priorisation de trafic.\n• **Adaptabilité :** Ajuste dynamiquement la taille des fichiers pour ne pas saturer les connexions faibles tout en exploitant pleinement le gigabit.\n• **Pur HTML5 :** Fonctionne sans aucune installation sur PC, smartphone ou Smart TV depuis n'importe quel navigateur.\n\n**Algorithme :**\n1. **Latence (Ping / Jitter) :** Envoi de micro-requêtes vers le serveur le plus proche parmi plus de 100 pour mesurer la stabilité.\n2. **Téléchargement (Download) :** Récupère des blocs de données (de 128 Ko à 128 Mo) de taille croissante jusqu'à ce que le transfert dépasse 8 secondes pour mesurer le débit de pointe.\n3. **Envoi (Upload) :** Processus inverse chargeant des fichiers de taille croissante pour mesurer le débit montant maximal stable."
                "it" -> "**Concetto:**\nSpeedof.me è uno strumento interamente basato sul browser (HTML5) per misurare la reale velocità e stabilità della connessione. Si differenzia dai test tradizionali perché evita Flash, Java o WebSockets, simulando l'autentica navigazione utente tramite download sequenziali tramite normali richieste HTTP.\n\n**Principio:**\nIl test si basa su un approccio incrementale in condizioni quotidiane reali:\n• **Nessun trucco di routing:** I file vengono scaricati tramite protocollo HTTP standard, impedendo ai gestori di applicare priorità artificiali o caching.\n• **Adattabilità:** Varia in tempo reale la dimensione dei file scaricati per non sovraccaricare le linee lente né limitare le connessioni gigabit.\n• **Puro HTML5:** Funziona senza programmi o app su PC, cellulari o Smart TV tramite qualsiasi browser moderno.\n\n**Algoritmo:**\n1. **Latenza (Ping / Jitter):** Serie di micro-richieste sul server più vicino tra gli oltre 100 disponibili.\n2. **Download test:** Scarica blocchi da 128 KB fino a 128 MB aumentando la grandezza finché il tempo supera gli 8 secondi per calcolare la velocità di picco.\n3. **Upload test:** Processo speculare che invia pacchetti via via più grandi per registrare la reale velocità di invio dati."
                "pt" -> "**Conceito:**\nSpeedof.me é uma ferramenta baseada totalmente no navegador (HTML5) para medir a velocidade e estabilidade reais da conexão. Difere de testes clássicos ao evitar Flash, Java ou WebSockets, simulando a navegação web real mediante descargas sequenciais de arquivos por meio de requisições HTTP comuns.\n\n**Princípio:**\nO teste baseia-se num método progressivo para testar a conexão nas condições do dia a dia:\n• **Sem aceleração artificial:** Arquivos baixados diretamente por HTTP padrão, impedindo que os provedores usem truques de cache ou priorização.\n• **Adaptabilidade:** Ajusta o tamanho dos arquivos de forma contínua para não sobrecarregar redes fracas nem limitar redes gigabit.\n• **HTML5 Puro:** Roda sem instalar apps, direto no navegador do PC, celular ou Smart TV.\n\n**Algoritmo:**\n1. **Latência (Ping / Jitter):** Micro-requisições ao servidor mais próximo entre 100+ disponíveis para medir a estabilidade.\n2. **Teste de Download:** Baixa blocos sucessivos de 128 KB a 128 MB, crescendo até que o download dure aprox. 8 segundos, encontrando a velocidade de pico.\n3. **Teste de Upload:** Envio reverso de pacotes crescentes para servidor a fim de mensurar a capacidade máxima estável."
                "da" -> "**Koncept:**\nSpeedof.me er et rent browserbaseret (HTML5) værktøj til måling af den reelle hastighed og stabilitet på din internetforbindelse. Testen adskiller sig ved at undgå brug af Flash, Java eller WebSockets, og simulerer i stedet reel web-surfing via sekventiel hentning af filer over almindelig HTTP.\n\n**Princip:**\nTesten bygger på en trinvis metode for at teste forbindelsen under de mest realistiske hverdagsforhold:\n• **Ingen kunstig acceleration:** Filer hentes direkte via standard HTTP-protokol, så udbydere ikke kan bruge mellemlagring eller prioritering.\n• **Tilpasningsevne:** Justerer løbende størrelsen på filerne for ikke at overbelaste langsomme net eller begrænse gigabit-forbindelser.\n• **Rent HTML5:** Kører uden app-installation på computere, mobiler eller Smart TV direkte i browseren.\n\n**Algoritme:**\n1. **Latens (Ping / Jitter):** Mikrosekunds-forespørgsler til den nærmeste af over 100 servere for at teste ping-stabilitet.\n2. **Download-test:** Henter filblokke (fra 128 KB op til 128 MB) med stigende størrelse, til overførslen varer ca. 8 sekunder for at måle spidshastigheden.\n3. **Upload-test:** Spejlvendt proces, der sender stigende mængder data afsted for at finde den maksimale stabile uploadhastighed."
                "fi" -> "**Konsepti:**\nSpeedof.me on täysin selainpohjainen (HTML5) työkalu internet-yhteyden todellisen nopeuden ja vakauden mittaamiseen. Se eroaa tutuista testeistä välttämällä Flashin, Javan tai WebSocketin käyttöä ja matkimalla aitoa verkkoselaamista lataamalla tiedostoja vaiheittain tavallisten HTTP-pyyntöjen kautta.\n\n**Periaate:**\nTesti etenee helposta vaikeampaan ja analysoi yhteyttä normaaleissa arkiolosuhteissa:\n• **Ei keinotekoista optimointia:** Tiedostot ladataan suoraan HTTP-protokollalla, mikä estää operaattoreita käyttämästä välimuistia tai priorisointia.\n• **Mukautuvuus:** Säätää tiedostokokoja lennosta, jotta hitaat linjat eivät tukkiudu eikä gigabitin kuituyhteyksiä rajoiteta.\n• **Puhdas HTML5:** Toimii ilman sovellusasennuksia tietokoneilla, puhelimilla tai Smart TV -laitteilla millä tahansa selaimella.\n\n**Algoritmi:**\n1. **Viiveen mittaus (Ping / Jitter):** Lähettää mikropyyntöjä lähimmälle yli sadasta palvelimesta mitatakseen pingin vakauden.\n2. **Lataustesti (Download):** Lataa lohkoja 128 kt koosta alkaen aina 128 Mt asti siten, että lohkon lataus kestää noin 8 sekuntia huippunopeuden paikantamiseksi.\n3. **Lähetystesti (Upload):** Vastaava prosessi, jossa laite lähettää palvelimelle kasvavia datapaketteja vakaan lähetysnopeuden mittaamiseksi."
                "kk" -> "**Табиғаты:**\nSpeedof.me — интернет қосылымының нақты жылдамдығы мен тұрақтылығын өлшеуге арналған толығымен браузерлік (HTML5) құрал. Оның басқа сынақтардан басты айырмашылығы — Flash, Java немесе WebSocket-ті қолданбай, әдеттегі HTTP сұраулары арқылы файлдарды кезекпен жүктеу арқылы нақты веб-серфингті модельдеуі болып табылады.\n\n**Принципі:**\nСынақ «қарапайымнан күрделіге» принципіне негізделген және қосылымды күнделікті сайттарды жүктеу жағдайында тексереді:\n• **Жасанды жеделдетусіз:** Файлдар тікелей стандартты HTTP арқылы жүктеліп, провайдерлердің трафикті жасанды басымдықпен немесе кэшпен көрсетуіне жол бермейді.\n• **Бейімділік:** Сынақ әлсіз желілерді шамадан тыс жүктемей және гигабиттік желілерді шектемей, арнаның қуатына сәйкес файл көлемін автоматты түрде таңдайды.\n• **Таза HTML5:** Кез келген құрылғыда (ПК, смартфондар, Smart TV) қосымша орнатпай, заманауи браузер арқылы жұмыс істейді.\n\n**Алгоритмі:**\n1. **Кідірісті анықтау (Ping / Jitter):** Пинг тұрақтылығын тексеру үшін 100-ден астам сервердің ең жақынына шағын сұраулар жіберу.\n2. **Жүктеу сынағы (Download):** 128 КБ-тан бастап 128 МБ-қа дейінгі файл блоктарын жүктеп, блок жүктелуі 8 секундқа жеткенге дейін өсіру арқылы ең жоғары жылдамдықты анықтайды.\n3. **Жіберу сынағы (Upload):** Жиналатын файл көлемін өсіре отырып, құрылғыдан серверге кері жіберу арқылы максималды тұрақты жіберу жылдамдығын тіркейді."
                "lt" -> "**Esmė:**\nSpeedof.me yra visiškai naršyklinis (HTML5) įrankis, skirtas išmatuoti realų interneto ryšio greitį ir stabilumą. Šis testas skiriasi nuo tradicinių tuo, kad nenaudoja Flash, Java ar WebSocket technologijų, o imituoja realų vartotojo naršymą, nuosekliai siųsdamasis failus per paprastas HTTP užklausas.\n\n**Principas:**\nTestas remiasi principu „nuo paprasto prie sudėtingo“ ir tikrina ryšį kasdienėmis naršymo sąlygomis:\n• **Be dirbtinio greitinimo:** Failai siunčiasi tiesiogiai per standartinį HTTP protokolą, todėl operatoriai negali pritaikyti talpyklos ar srauto prioriteto nustatymų.\n• **Prisitaikymas:** Dinamiškai keičia failų dydį, neperkraudamas silpno ryšio linijų ir neribodamas gigabitinių jungčių spartos.\n• **Tikras HTML5:** Veikia kompiuteriuose, telefonuose bei Smart TV bei jokių programėlių įrašymo – tiesiog bet kurioje naršyklėje.\n\n**Algoritmas:**\n1. **Delsos matavimas (Ping / Jitter):** Mikro užklausų siuntimas į artimiausią iš daugiau nei 100 serverių ping stabilumui įvertinti.\n2. **Atsisiuntimas (Download):** Siunčiasi duomenų blokus nuo 128 KB iki 128 MB, kol vieno bloko siuntimas užtrunka ~8 sekundes, taip nustatant maksimalią spartą.\n3. **Išsiuntimas (Upload):** Atvirkštinis veidrodinis procesas, kuomet į serverį siunčiami didėjantys failų paketai išsiuntimo greičiui užfiksuoti."
                "lv" -> "**Būtība:**\nSpeedof.me ir pilnībā pārlūkprogrammā bāzēts (HTML5) rīks interneta reālā ātruma un stabilitātes mērīšanai. Tā galvenā atšķirība no klasiskajiem testiem ir atteikšanās no Flash, Java vai WebSocket, tā vietā imitējot reālu mājas sērfošanu ar secīgu failu lejupielādi caur parastiem HTTP pieprasījumiem.\n\n**Princips:**\nTests balstās uz principu „no vienkāršā uz sarežģīto” un pārbauda savienojumu reālos ikdienas sērfošanas apstākļos:\n• **Bez mākslīga paātrinājuma:** Faili tiek lejupielādēti tieši caur standarta HTTP protokolu, kas neļauj operatoriem izmantot prioritātes vai kešatmiņas trikus.\n• **Pielāgojamība:** Dinamiski maina failu izmērus, lai nepārslogotu vājus savienojumus un neierobežotu gigabitu līnijas.\n• **Tīrs HTML5:** Darbojas bez aplikāciju uzstādīšanas jebkurā datorā, telefonā vai Smart TV, izmantojot modernu pārlūkprogrammu.\n\n**Algoritms:**\n1. **Aiztures tests (Ping / Jitter):** Sūta mikro pieprasījumus uz tuvāko no vairāk nekā 100 serveriem ping stabilitātes noteikšanai.\n2. **Lejupielādes tests (Download):** Lejupielādē blokus, sākot no 128 KB līdz pat 128 MB, audzējot apjomu, līdz bloka lejupielāde aizņem ~8 sekundes maksimālā ātruma fiksēšanai.\n3. **Augšupielādes tests (Upload):** Simetrisks process, sūtot atpakaļ uz serveri pieaugoša izmēra failus stabilāko augšupielādes datu fiksēšanai."
                "sv" -> "**Koncept:**\nSpeedof.me är ett helt webbläsarbaserat (HTML5) verktyg för att mäta den faktiska hastigheten och stabiliteten hos din internetanslutning. Det skiljer sig från klassiska tester genom att avstå från Flash, Java eller WebSocket till förmån för att simulera verkligt surfande med sekventiella filhämtningar via vanliga HTTP-anrop.\n\n**Princip:**\nTestet bygger på principen ”från enkelt till svårt” och testar anslutningen under normala vardagliga förhållanden:\n• **Ingen artificiell acceleration:** Filer laddas ner direkt via standard HTTP, vilket hindrar operatörer från att använda cachelagring eller prioriteringstrafik för kända mätserverar.\n• **Anpassning:** Justerar filstorlekar dynamiskt så att svaga anslutningar inte överbelastas och gigabit-hastigheter inte begränsas artificiellt.\n• **Ren HTML5:** Fungerar utan app-installationer på PC, mobil eller Smart-TV direkt i din moderna webbläsare.\n\n**Algoritm:**\n1. **Latenstest (Ping / Jitter):** Skickar mikroskopiska förfrågningar till den närmaste av över 100 servrar för att mäta svarstiden.\n2. **Nedladdningstest (Download):** Hämtar datablock från 128 KB upp till 128 MB med stigande storlek tills nedladdningen tar ca 8 sekunder för att hitta spetshastigheten.\n3. **Uppladdningstest (Upload):** Spegelvänd process som skickar datafiler till servern i stigande storlekar för att fastställa den maximala stabila uppladdningshastigheten."
                else -> "**Concept:**\nSpeedof.me is a fully browser-based (HTML5) tool to measure real speed and stability of your internet connection. It differs from classic tests (like Ookla Speedtest) by avoiding Flash, Java, or WebSockets, and simulating real user browsing by sequentially downloading files via standard HTTP requests.\n\n**Key principles:**\n• **No artificial acceleration:** Files download directly via standard HTTP protocol, preventing ISPs from using \"cheat codes\" like caching or prioritization.\n• **Adaptability:** Explores bandwidth by dynamically adjusting file sizes to avoid overloading weak lines or artificially capping gigabit speeds.\n• **Pure HTML5:** Run without any app installations on any device (PC, phone, Smart TV) via any modern browser.\n\n**Algorithm:**\n1. **Ping / Jitter calculation:** Sends micro-requests to the nearest of 100+ servers to test response times.\n2. **Download test (Download):** Downloads consecutive blocks (starting at 128 KB up to 128 MB) until the block duration exceeds ~8 seconds to find peak speed.\n3. **Upload test (Upload):** A mirrored uploading process of file blocks (from smaller to larger) measuring peak upload speed."
            }
            "iperf" -> IperfLocalizations.getDialogBody(normalizedLang)
            "wifi_analyzer_fullscreen" -> when (normalizedLang) {
                "ru" -> "Для улучшения работы Анализатор WiFi, в разделе «Для разработчиков» настроек Android вам необходимо найти и выключить (деактивировать) опцию:\n\n«Ограничение запросов Wi-Fi» (в некоторых версиях Android она называется «Ограничение сканирования Wi-Fi» или Wi-Fi scan throttling).\n\nПочему это важно?\n\nКак это работает: По умолчанию Android ограничивает частоту сканирования беспроводных сетей фоновыми и даже активными приложениями (не чаще 4 раз в несколько минут), чтобы экономить заряд батареи.\n\nЕсли эта опция включена, «Анализатор WiFi» будет обновлять графики каналов и уровень сигнала с огромной задержкой, либо вообще перестанет показывать изменения в реальном времени.\n\nЕсли вы её выключите, приложение сможет сканировать эфир непрерывно, мгновенно реагируя на ваши перемещения по комнате и смену частот.\n\nКак её найти (пошагово):\n\nОткройте Настройки телефона.\n\nПерейдите в раздел «Для разработчиков» (если он не активен, предварительно зайдите в «О телефоне» и нажмите на «Номер сборки» 7 раз).\n\nПролистайте вниз до блока «Сети» (Networking).\n\nНайдите пункт «Ограничение запросов Wi-Fi» (Wi-Fi scan throttling) и переведите переключатель в выключенное положение.\n\nПримечание: Отключение этой функции может незначительно увеличить расход батареи, пока анализатор запущен, но для точной настройки роутера это критически важно. После работы с анализатором опцию можно включить обратно."
                "uk" -> "Для покращення роботи Аналізатора WiFi, в розділі «Для розробників» налаштувань Android вам необхідно знайти та вимкнути (деактивувати) опцію:\n\n«Обмеження запитів Wi-Fi» (в деяких версіях Android вона називається «Обмеження сканування Wi-Fi» або Wi-Fi scan throttling).\n\nЧому це важливо?\n\nЯк це працює: За замовчуванням Android обмежує частоту сканування бездротових мереж фоновими і навіть активними програмами (не частіше ніж 4 рази на кілька хвилин), щоб економити заряд батарєї.\n\nЯкщо ця опція увімкнена, «Аналізатор WiFi» оновлюватиме графіки каналів та рівень сигналу з величезною затримкою, або взагалі припинить показувати зміни в реальному часі.\n\nЯкщо ви її вимкнете, програма зможе сканувати ефір безперервно, миттєво реагуючи на ваші переміщення кімнатою та зміну частот.\n\nЯк її знайти (покроково):\n\nВідкрийте Налаштування телефону.\n\nПерейдіть до розділу «Для розробників» (якщо він не активний, заздалегідь зайдіть до «Про телефон» та натисніть на «Номер збірки» 7 разів).\n\nПролистайте вниз до блоку «Мережі» (Networking).\n\nЗнайдіть пункт «Обмеження запитів Wi-Fi» (Wi-Fi scan throttling) та переведіть перемикач у вимкнене положення.\n\nПримітка: Вимкнення цієї функції може незначно збільшити витрату батареї, поки аналізатор запущено, але для точного налаштування роутера це критично важливо. Після роботи з аналізатором опцію можна увімкнути назад."
                "be" -> "Для паляпшэння працы Аналізатара WiFi, у раздзеле «Для распрацоўшчыкаў» налад Android вам неабходна знайсці і выключыць (дэактываваць) опцыю:\n\n«Абмежаванне запытаў Wi-Fi» (у некаторых версіях Android яна называецца «Абмежаванне сканавання Wi-Fi» або Wi-Fi scan throttling).\n\nЧаму гэта важна?\n\nЯк гэта працуе: Па змаўчанні Android абмяжоўвае частату сканавання бесправадных сетак фонавымі і нават актыўнымі праграмамі (не часцей за 4 разы ў некалькі хвілін), каб эканоміць зарад батарэі.\n\nКалі гэтая опцыя ўключана, «Аналізатар WiFi» будзе абнаўляць графікі каналаў і ўзровень сігналу з велізарнай затрымкай, альбо наогул перастане паказваць змены ў рэальным часе.\n\nКалі вы яе выключыце, праграма зможа сканаваць эфір бесперапынна, імгненна рэагуючы на ​​вашы перамяшчэнні па пакоі і змену частот.\n\nЯк яе знайсці (пакрокава):\n\nАдкрыйце Налады тэлефона.\n\nПеренайдзіце ў раздзел «Для распрацоўшчыкаў» (калі ён не актыўны, папярэдне зайдзіце ў «Пра тэлефон» і націсніце на «Нумар зборкі» 7 разоў).\n\nПрагартайце ўніз да блока «Сеткі» (Networking).\n\nЗнайдзіце пункт «Абмежаванне запытаў Wi-Fi» (Wi-Fi scan throttling) і перавядзіце пераключальнік у выключанае становішча.\n\nЗаўвага: Адключэнне гэтай функцыі можа нязначна павялічыць расход батарэі, пакуль аналізатар запушчаны, але для дакладнай налады роўтара гэта крытычна важна. Пасля працы з аналізатарам опцыю можна ўключыць назад."
                "de" -> "Um die Leistung des Wi-Fi-Analysators zu verbessern, müssen Sie in den Android-Entwickleroptionen die folgende Option suchen und deaktivieren:\n\n„WLAN-Scan-Drosselung“ (je nach Android-Version auch als „Einschränkung von Wi-Fi-Anfragen“ bezeichnet).\n\nWarum ist das wichtig?\n\nWie es funktioniert: Standardmäßig beschränkt Android die Häufigkeit von Scans nach drahtlosen Netzwerken durch Hintergrund- und sogar aktive Anwendungen (nicht mehr als 4 Mal in wenigen Minuten), um Batteriestrom zu sparen.\n\nWenn diese Option aktiviert ist, aktualisiert der „Wi-Fi Analysator“ Kanalkurven und Signalstärken mit enormer Verzögerung oder zeigt Echtzeitänderungen gar nicht mehr an.\n\nWenn Sie sie deaktivieren, kann die App das Spektrum kontinuierlich scannen und reagiert sofort auf Ihre Bewegungen im Raum und Frequenzwechsel.\n\nSchritt-für-Schritt-Anleitung:\n\nÖffnen Sie die Einstellungen Ihres Telefons.\n\nGehen Sie zu „Entwickleroptionen“ (wenn nicht aktiv, gehen Sie zuerst auf „Über das Telefon“ und tippen Sie 7 Mal auf die „Build-Nummer“).\n\nScrollen Sie nach unten zum Abschnitt „Netzwerke“ (Networking).\n\nSuchen Sie den Eintrag „WLAN-Scan-Drosselung“ (Wi-Fi scan throttling) und schalten Sie den Schalter aus.\n\nHinweis: Das Deaktivieren dieser Funktion kann den Akkuverbrauch leicht erhöhen, solange der Analysator läuft, ist jedoch für die präzise Netzwerkkonfiguration des Routers absolut kritisch. Danach kann sie wieder aktiviert werden."
                "es" -> "Para mejorar el funcionamiento de Analizador Wi-Fi, en la sección «Opciones para desarrolladores» de la configuración de Android debes buscar y desactivar la siguiente opción:\n\n«Limitación de búsqueda de Wi-Fi» (en algunas versiones de Android se llama «Limitación de escaneo de Wi-Fi» o Wi-Fi scan throttling).\n\n¿Por qué es importante?\n\nCómo funciona: Por defecto, Android limita la frecuencia con la que las aplicaciones en segundo plano e incluso activas escanean redes inalámbricas (no más de 4 veces cada pocos minutos) para ahorrar batería.\n\nSi esta opción está activada, el «Analizador Wi-Fi» actualizará los gráficos de canales y la intensidad de la señal con un retraso enorme, o dejará de mostrar cambios en tiempo real por completo.\n\nSi la desactivas, la aplicación podrá escanear el espectro de forma continua, respondiendo instantáneamente a tus movimientos por la sala y cambios de frecuencia.\n\nGuía paso a paso para encontrarla:\n\nAbre los Ajustes del teléfono.\n\nVe a «Opciones para desarrolladores» (si no está activo, ve primero a «Acerca del teléfono» y toca 7 veces en «Número de compilación»).\n\nDesplázate hacia abajo hasta la sección «Redes» (Networking).\n\nBusca la opción «Limitación de búsqueda de Wi-Fi» (Wi-Fi scan throttling) y apaga el interruptor.\n\nNota: Desactivar esta función puede aumentar ligeramente el consumo de batería mientras el analizador está en ejecución, pero es fundamental para la configuración precisa del enrutador. Puedes volver a activarla después de usar la aplicación."
                "fr" -> "Pour améliorer les performances de l'Analyseur Wi-Fi, vous devez rechercher et désactiver l'option suivante dans les « Options pour les développeurs » d'Android :\n\n« Limitation de la recherche Wi-Fi » (selon les versions d'Android, appelée « Limitation du balayage Wi-Fi » ou Wi-Fi scan throttling).\n\nPourquoi est-ce important ?\n\nComment ça marche : Par défaut, Android limite la fréquence des analyses de réseaux sans fil par les applications en arrière-plan et même actives (pas plus de 4 fois en quelques minutes) afin d'économiser la batterie.\n\nSi cette option est activée, l'« Analyseur Wi-Fi » mettra à jour les graphiques de canaux et la puissance du signal avec un retard énorme, voire cessera complètement d'afficher les modifications en temps réel.\n\nSi vous la désactivez, l'application pourra balayer le spectre en continu, réagissant instantanément à vos déplacements dans la pièce et aux changements de fréquences.\n\nComment la trouver pas à pas :\n\nOuvrez les Paramètres du téléphone.\n\nAllez dans « Options pour les développeurs » (si non activé, allez d'abord dans « À propos du téléphone » et appuyez 7 fois sur « Numéro de build »).\n\nFaites défiler vers le bas jusqu'au bloc « Réseau » (Networking).\n\nRecherchez l'option « Limitation de la recherche Wi-Fi » (Wi-Fi scan throttling) et désactivez le commutateur.\n\nRemarque : La désactivation de cette fonctionnalité peut augmenter légèrement la consommation de la batterie pendant l'exécution de l'analyseur, mais elle est cruciale pour un réglage précis du routeur. Vous pourrez la réactiver après utilisation."
                "it" -> "Per migliorare il funzionamento dell'Analizzatore Wi-Fi, nella sezione «Opzioni sviluppatore» di Android è necessario trovare e disattivare la seguente opzione:\n\n«Limitazione ricerca Wi-Fi» (in alcune versioni di Android denominata «Limitazione scansione Wi-Fi» o Wi-Fi scan throttling).\n\nPerché è importante?\n\nCome funziona: Per impostazione predefinita, Android limita la frequenza delle scansioni delle reti wireless da parte delle app in background e persino attive (non più di 4 volte in pochi minuti) per risparmiare batteria.\n\nSe questa opzione è attiva, l'«Analizzatore Wi-Fi» aggiornerà i grafici dei canali e l'intensità del segnale con un ritardo enorme, o smetterà del tutto di mostrare le modifiche in tempo reale.\n\nSe la disattivi, l'applicazione potrà eseguire scansioni continue dello spettro, rispondendo all'istante ai tuoi spostamenti nella stanza e ai cambi di frequenza.\n\nCome trovarla passo dopo passo:\n\nApri le Impostazioni del telefono.\n\nVai alla sezione «Opzioni sviluppatore» (se non è attiva, vai prima su «Informazioni sul telefono» e premi 7 volte il «Numero build»).\n\nScorri verso il basso fino alla sezione «Reti» (Networking).\n\nTrova l'opzione «Limitazione ricerca Wi-Fi» (Wi-Fi scan throttling) e disattiva l'interruttore.\n\nNota: La disattivazione di questa opzione potrebbe aumentare leggermente il consumo della batteria mentre l'analizzatore è in esecuzione, ma è di fondamentale importanza per ottimizzare accuratamente il router. Al termine puoi riattivarla."
                "pt" -> "Para melhorar o desempenho do Analisador Wi-Fi, na seção «Opções do desenvolvedor» do Android você precisa encontrar e desativar a seguinte opção:\n\n«Limitação de busca de Wi-Fi» (em algumas versões do Android chamada «Limitação de escaneamento Wi-Fi» ou Wi-Fi scan throttling).\n\nPor que isso é importante?\n\nComo funciona: Por padrão, o Android limita a frequência de escaneamento de redes sem fio por aplicativos em segundo plano e até mesmo ativos (não mais que 4 vezes a cada poucos minutos) para economizar bateria.\n\nSe esta opção estiver ativada, o «Analisador Wi-Fi» atualizará os gráficos de canais e a força do sinal com um atraso enorme, ou deixará de mostrar as alterações em tempo real.\n\nComo encontrar o item passo a passo:\n\nAbra as Configurações do telefone.\n\nVá para a seção «Opções do desenvolvedor» (se não estiver ativa, primeiro acesse «Sobre o telefone» e toque em «Número da versão» 7 vezes).\n\nRole para baixo até o bloco «Redes» (Networking).\n\nEncontre o item «Limitação de busca de Wi-Fi» (Wi-Fi scan throttling) e desligue o interruptor.\n\nNota: Desativar esta função pode aumentar ligeiramente o consumo de bateria enquanto o analisador está aberto, mas é crucial para ajustar o roteador com precisão. Você pode reativá-la depois."
                "da" -> "For at forbedre effektiviteten af Wi-Fi Analysator skal du finde og deaktivere følgende indstilling under Androids «Udviklerindstillinger»:\n\n«Begrænsning af Wi-Fi-scanning» (kendes også som Wi-Fi scan throttling).\n\nHvorfor er det vigtigt?\n\nHvordan det virker: Som standard begrænser Android frekvensen af scanninger efter trådløse netværk foretaget af baggrundsapps og selv aktive apps (højst 4 gange på få minutter) for at spare strøm.\n\nHvis denne funktion er aktiveret, vil „Wi-Fi Analysator“ opdatere kanalgrafer og signalstyrker med en enorm forsinkelse eller helt stoppe med at vise ændringer i realtid.\n\nHvis du deaktiverer den, kan appen scanne spektret kontinuerligt, hvilket giver øjeblikkelig respons på dine bevægelser i rummet samt frekvensskift.\n\nTrin-for-trin vejledning:\n\nÅbn telefonens Indstillinger.\n\nGå til «Udviklerindstillinger» (hvis ikke aktiv, skal du først gå to «Om telefonen» og trykke på «Versionsnummer»/«Build-nummer» 7 gange).\n\nRul ned til sektionen «Netværk» (Networking).\n\nFind punktet «Begrænsning af Wi-Fi-scanning» (Wi-Fi scan throttling) og slå knappen fra.\n\nBemærk: Deaktivering af denne funktion kan øge batteriforbruget en smule, mens analysatoren kører, men det er kritisk for en nøjagtig routeropsætning. Du kan slå den til igen bagefter."
                "fi" -> "Parantaaksesi Wi-Fi-analysaattorin toimintaa sinun on etsittävä ja poistettava käytöstä seuraava asetus Androidin ”Kehittäjäasetukset”-valikosta:\n\n”Wi-Fi-haun rajoitus” (joissakin Android-versioissa nimeltään ”Wi-Fi-haun kuristus” tai ”Wi-Fi scan throttling”).\n\nMiksi tämä on tärkeää?\n\nMiten se toimii: Oletuksena Android rajoittaa taustalla ja jopa aktiivisena olevien sovellusten langattomien verkkojen hakutiheyttä (enintään 4 kertaa muutamassa minuutissa) säästääkseen akkuvirtaa.\n\nJos tämä asetus on käytössä, ”Wi-Fi-analysaattori” päivittää kanavakaaviot ja signaalin voimakkuuden erittäin suurella viiveellä tai lakkaa kokonaan näyttämästä reaaliaikaisia muutoksia.\n\nJos poistat sen käytöstä, sovellus voi suorittaa jatkuvaa hakuvalvontaa reagoimalla heti liikkeisiisi huoneessa ja taajuusmuutoksiin.\n\nMiten löydät sen (vaiheittain):\n\nAvaa puhelimen Asetukset.\n\nSiirry kohtaan ”Kehittäjäasetukset” (jos se ei ole aktiivinen, siirry ensin kohtaan ”Tietoja puhelimesta” ja napauta ”Ohjelmistoversion numeroa” 7 kertaa).\n\nVieritä alaspäin ”Verkkoyhteydet” (Networking) -kohtaan.\n\nEtsi kohta ”Wi-Fi-haun rajoitus” (Wi-Fi scan throttling) ja aseta kytkin pois päältä.\n\nHuomautus: Tämän toiminnon poistaminen käytöstä voi lisätä hieman akun kulutusta analysaattorin ollessa käynnissä, mutta se on kriittisen tärkeää reitittimen tarkassa määrityksessä. Voit ottaa sen uudelleen käyttöön käytön jälkeen."
                "kk" -> "Wi-Fi Анализаторының жұмысын жақсарту үшін Android параметрлеріндегі «Әзірлеушілер үшін» бөлімінде келесі параметрді тауып, өшіруіңіз (деактивациялауыңыз) керек:\n\n«Wi-Fi сұрауларын шектеу» (кейбір Android нұсқаларында ол «Wi-Fi сканерлеуді шектеу» немесе Wi-Fi scan throttling деп аталады).\n\nБұл неге маңызды?\n\nОл қалай жұмыс істейді: Әдепкі бойынша Android батарея зарядын үнемдеу үшін фондық және тіпті белсенді қолданбалардың сымсыз желілерді сканерлеу жиілігін шектейді (бірнеше минутта 4 реттен көп емес).\n\nЕгер бұл параметр қосулы болса, «Wi-Fi Анализаторы» арналар графиктері мен сигнал деңгейін өте үлкен кідіріспен жаңартады немесе мүлдем нақты уақыттағы өзгерістерді көрсетуді тоқтатады.\n\nЕгер оны өшірсеңіз, қолданба эфирді үздіксіз сканерлеп, бөлмедегі қозғалыстарыңыз бен жиіліктердің өзгеруіне бірден жауап бере алады.\n\nОны қадамдық түрде қалай табуға болады:\n\nТелефон параметрлерін ашыңыз.\n\n«Әзірлеушілер үшін» бөліміне өтіңіз (егер ол белсенді болмаса, алдымен «Телефон туралы» бөліміне өтіп, «Құрастыру нөмірін» 7 рет басыңыз).\n\nТөмен қарай «Желілер» (Networking) блогына өтіңіз.\n\n«Wi-Fi сұрауларын шектеу» (Wi-Fi scan throttling) тармағын тауып, ауыстырып-қосқышты өшіріңіз.\n\nЕскертпе: Бұл функцияны өшіру анализатор жұмыс істеп тұрған кезде батарея шығынын шамалы арттыруы мүмкін, бірақ роутерді дәл баптау үшін бұл өте маңызды. Жұмыс аяқталғаннан кейін оны қайта қосуға болады."
                "lt" -> "Norint pagerinti „Wi-Fi analizatoriaus“ darbą, „Android“ nustatymų skyriuje „Kūrėjo parinktys“ turite surasti ir išjungti (deaktyvuoti) šią parinktį:\n\n„Wi-Fi paieškos ribojimas“ (kai kuriose „Android“ versijose vadinama „Skenavimo ribojimu“ arba „Wi-Fi scan throttling“).\n\nKodėl tai svarbu?\n\nKaip tai veikia: Pagal numatytuosius nustatymus „Android“ riboja belaidžių tinklų paieškos dažnį foninėms ir net aktyvioms programoms (ne dažniau kaip 4 kartus per kelias minutes), kad taupytų akumuliatoriaus energiją.\n\nJei ši parinktis įjungta, „Wi-Fi analizatorius“ kanalų grafikus ir signalo lygį atnaujins su didžiuliu vėlavimu arba aplinkai keičiantis išvis nerodys jokių realaus laiko pokyčių.\n\nJei ją išjungsite, programa galės skenuoti eterį nepertraukiamai, akimirksniu reaguodama į jūsų judėjimą kambaryje ir dažnių pasikeitimus.\n\nKaip ją rasti žingsnis po žingsnio:\n\nAtidarykite telefono Nustatymus.\n\nEikite į skyrių „Kūrėjo parinktys“ (jei jis neaktyvus, pirmiausia eikite į „Apie telefoną“ ir 7 kartus paspauskite „Kompiuterio numerį“ / „Build number“).\n\nSlinkite žemyn iki bloko „Tinklai“ (Networking).\n\nRaskite elementą „Wi-Fi paieškos ribojimas“ (Wi-Fi scan throttling) ir perjunkite jungiklį į išjungimo padėtį.\n\nPastaba: Šios funkcijos išjungimas gali nežymiai padidinti akumuliatoriaus energijos suvartojimą, kol veikia analizatorius, tačiau norint tiksliai suderinti maršruto parinktuvą, tai yra kritiškai svarbu. Po darbo su analizatoriumi parinktį galima vėl įjungti."
                "lv" -> "Lai uzlabotu Wi-Fi analizatora darbību, Android iestatījumu sadaļā «Izstrādātāja opcijas» jums ir jāatrod un jāizslēdz (jādeaktivizē) šāda opcija:\n\n«Wi-Fi meklēšanas ierobežošana» (dažās Android versijās to sauc par «Wi-Fi skenēšanas droselēšanu» jeb Wi-Fi scan throttling).\n\nKāpēc tas ir svarīgi?\n\nKā tas darbojas: Pēc noklusējuma Android ierobežo bezvadu tīklu skenēšanas biežumu fona un pat aktīvām lietotnēm (ne biežāk kā 4 reizes dažu minūšu laikā), lai taupītu akumulatora enerģiju.\n\nJa šī opcija ir ieslēgta, «Wi-Fi analizators» kanālu grafikus un signāla līmeni atjauninās ar milzīgu aizkavēšanos vai vispār pārstās rādīt izmaiņas reāllaikā.\n\nJa jūs to izslēgsiet, lietotne varēs skenēt ēteru nepārtraukti, acumirklī reaģējot uz jūsu pārvietošanos telpā un frekvenču maiņu.\n\nKā to atrast soli pa solim:\n\nAtballstiet tālruņa Iestatījumus.\n\nDodieties uz sadaļu «Izstrādātāja opcijas» (ja tā nav aktīva, vispirms dodieties uz «Par tālruni» un 7 reizes nospiediet «Atgādinājuma numuru» / «Build number»).\n\nRitiniet uz leju līdz blokam «Tīkli» (Networking).\n\nAtrodiet vienumu «Wi-Fi meklēšanas ierobežošana» (Wi-Fi scan throttling) un pārslēdziet slēdzi izslēgta stāvoklī.\n\nPiezīme: Šīs funkcijas atspējošana var nedaudz palielināt akumulatora patēriņu analizatora darbības laikā, taču precīzai maršrutētāja iestatīšanai tas ir kritiski svarīgi. Pēc darba ar analizatoru opciju var ieslēgt atpakaļ."
                "sv" -> "För att förbättra funktionen hos Wi-Fi-analysatorn måste du hitta och inaktivera följande alternativ under ”Utvecklaralternativ” i Android-inställningarna:\n\n”Begränsning av Wi-Fi-sökning” (kallas även ”Wi-Fi-skanningstrypning” eller ”Wi-Fi scan throttling” i vissa Android-versioner).\n\nVarför är detta viktigt?\n\nHur det fungerar: Som standard begränsar Android frekvensen av nätverksskanningar för bakgrundsappar och till och med aktiva appar (högst 4 gånger på några minuter) för att spara batteri.\n\nOm det här alternativet är aktiverat kommer ”Wi-Fi-analysatorn” att uppdatera kanalgrafer och signalstyrka med en enorm fördröjning, eller sluta visa realtidsändringar helt och hållet.\n\nOm du inaktiverar alternativet kan applikationen skanna luften kontinuerligt och reagera direkt på dina rörelser i rummet och frekvensändringar.\n\nSteg-för-steg-instruktion:\n\nÖppna telefonens Inställningar.\n\nGå till ”Utvecklaralternativ” (om det inte är aktivt, gå först till ”Om telefonen” och tryck på ”Pratnummer” / ”Build number” 7 gånger).\n\nBläddra ner till blocket ”Nätverk” (Networking).\n\nHitta objektet ”Begränsning av Wi-Fi-sökning” (Wi-Fi scan throttling) och stäng av strömbrytaren.\n\nObs: Att inaktivera den här funktionen kan öka batteriförbrukningen något medan analysatorn körs, men för exakt router-inställning är det kritiskt viktigt. Efter användning kan alternativet slås på igen."
                else -> "To improve WiFi Analyzer performance, in the Android Developer Options you need to find and disable (deactivate) the following option:\n\n\"Wi-Fi scan throttling\" (referred to as \"Limit Wi-Fi requests\" or \"Wi-Fi scan throttling\" depending on your Android version).\n\nWhy is this important?\n\nHow it works: By default, Android limits the frequency of wireless network scans by background and even active applications (no more than 4 times in a few minutes) to save battery power.\n\nIf this option is enabled, \"WiFi Analyzer\" will update channel graphs and signal levels with a huge delay, or stop showing real-time changes entirely.\n\nIf you disable it, the app can perform continuous scanning of the spectrum, instantly responding to your movements across the room and a change of frequencies.\n\nHow to find it (step-by-step):\n\nOpen your Phone Settings.\n\nGo to the \"Developer Options\" section (if not active, first go to \"About Phone\" and press \"Build Number\" 7 times).\n\nScroll down to the \"Networking\" section.\n\nFind \"Wi-Fi scan throttling\" and toggle it to the OFF position.\n\nNote: Disabling this option may slightly increase battery consumption while the analyzer is running, but it is critically important for tuning your router with precision. You can enable it back once you are done using the analyzer."
            }
            "wifi_analyzer" -> when (normalizedLang) {
                "ru" -> "**Суть:**\nМониторинг радиоэфира Wi-Fi и распределения беспроводных сетей по частотным каналам в реальном времени.\n\n**Принцип:**\nАнализатор сканирует окружающие беспроводные сети и отображает полученную информацию в виде параболических графиков:\n• **Мощность сигнала (dBm):** Чем выше пик параболы, тем сильнее сигнал сети. Типичные значения составляют от -30 dBm (очень сильный) до -90 dBm (очень слабый, на пороге обрыва).\n• **Ширина канала:** Ширина основания параболы соответствует ширине полосы пропускания сети (20, 40, 80 или 160 МГц).\n• **Активное подключение:** График сети, к которой устройство подключено в данный момент, выделяется **красным цветом**, а её SSID отображается в красной прямоугольной карточке для быстрого визуального поиска.\n\n**Диапазоны частот:**\n• **2.4 ГГц:** Дальнобойный диапазон с высокой проникающей способностью, но сильной зашумленностью (каналы от 1 до 13).\n• **5 ГГц:** Высокоскоростной диапазон с меньшими помехами и возможностью объединения каналов (каналы от 36 до 165).\n• **6 ГГц:** Спектр сверхвысоких скоростей Wi-Fi 6E/7, свободный от влияния старых стандартов."
                "uk" -> "**Суть:**\nМоніторинг радіоефіру Wi-Fi та розподілу бездротових мереж по частотних каналах у реальному часі.\n\n**Принцип:**\nАналізатор сканує бездротові мережі навколо та будує параболічні графіки:\n• **Потужність сигналу (dBm):** Чим вищий пік параболи, тим сильніший сигнал. Діапазон від -30 dBm (чудовий) до -90 dBm (критичний).\n• **Ширина каналу:** Ширина основи параболи вказує на смугу (20, 40, 80 чи 160 МГц).\n• **Активне підключення:** Ваша мережа виділена **червоним кольором** на графіку, а її назва виводиться в червоній рамці."
                "be" -> "**Сутнасць:**\nМаніторынг радыёэфіру Wi-Fi і размеркавання бесправадных сетак па частотных каналах у рэальным часе.\n\n**Прынцып:**\nАналізатар скануе навакольныя сеткі і адлюстроўвае параболы:\n• **Магутнасць сігналу (dBm):** Чым вышэй пік параболы, тым мацней сігнал. Ад -30 dBm (выдатны) да -90 dBm (вельмі слабы).\n• **Шырыня канала:** Адпавядае паласе (20, 40, 80 ці 160 МГц).\n• **Актыўнае падключэнне:** Графік падлучанай сеткі адлюстроўваецца **чырвоным колерам**, а назва змяшчаецца ў чырвоны прамавугольнік."
                "de" -> "**Konzept:**\nEchtzeit-Überwachung des Wi-Fi-Frequenzspektrums und der Kanalbelegung.\n\n• **Signalstärke (dBm):** Höhere Peaks zeigen stärkere Signale (-30 dBm bis -90 dBm).\n• **Kanalbreite:** Die Breite der Parabel zeigt die Bandbreite (20 bis 160 MHz).\n• **Aktive Verbindung:** Das aktuell verbundene Netz wird **rot** dargestellt und sein Name wird rot umrandet."
                "es" -> "**Concepto:**\nMonitoreo en tiempo real del espectro de radio Wi-Fi y la distribución de canales.\n\n• **Intensidad (dBm):** Los picos más altos representan señales más fuertes (-30 dBm a -90 dBm).\n• **Ancho de canal:** El ancho de la parábola indica el ancho de banda (20, 40, 80 o 160 MHz).\n• **Conexión activa:** Se resalta en **rojo** en el gráfico con su SSID en un rectángulo rojo."
                "fr" -> "**Concept :**\nSurveillance en temps réel du spectre radio Wi-Fi et de l'occupation des canaux.\n\n• **Puissance (dBm) :** Plus la courbe est haute, plus le signal est fort (-30 dBm à -90 dBm).\n• **Largeur de canal :** La base de la parabole indique la largeur de bande (20, 40, 80 ou 160 MHz).\n• **Connexion active :** Affichée en **rouge** sur le graphique, avec son nom encadré de rouge."
                "it" -> "**Concetto:**\nMonitoraggio in tempo reale dello spettro radio Wi-Fi e della congestione dei canali.\n\n• **Potenza (dBm):** I picchi descrivono un segnale più forte (-30 dBm a -90 dBm).\n• **Larghezza canale:** La larghezza della curva indica lo spettro occupato (20, 40, 80 o 160 MHz).\n• **Connessione attiva:** Evidenziata in **rosso** con il nome all'interno di un rettangolo rosso."
                "pt" -> "**Conceito:**\nMonitoramento em tempo real do espectro de canais Wi-Fi e sinais disponíveis.\n\n• **Potência (dBm):** Curvas mais altas indicam melhor recepção (-30 dBm a -90 dBm).\n• **Largura do canal:** Indica a largura de banda ocupada (20, 40, 80 ou 160 MHz).\n• **Conexão ativa:** Desenhada no gráfico em **vermelho**, com o SSID dentro de um cartão vermelho."
                "da" -> "**Koncept:**\nRealtidsovervågning af Wi-Fi radiomiljø og kanalfordeling.\n\n• **Signalstyrke (dBm):** Jo højere top, jo bedre signal (-30 dBm til -90 dBm).\n• **Kanalbredde:** Parablens bredde angiver båndbredden (20, 40, 80 eller 160 MHz).\n• **Aktiv forbindelse:** Vises med **rød** grafik, og dens SSID fremhæves i en rød boks."
                "fi" -> "**Konsepti:**\nWi-Fi -radiotaajuuksien ja kanavien ruuhkautumisen reaaliaikainen tarkastelu.\n\n• **Signaalin voimakkuus (dBm):** Korkeammat huiput tarkoittavat vakaampaa signaalia (-30 dBm - -90 dBm).\n• **Kanavan leveys:** Parabolin leveys osoittaa kaistanleveyden (20, 40, 80 tai 160 MHz).\n• **Aktiivinen yhteys:** Merkitään **punaisella** värillä kuvaajassa ja verkkonimi asetetaan punaiseen laatikkoon."
                "kk" -> "**Табиғаты:**\nНақты уақыттағы Wi-Fi жиілік спектрі мен арналардың таралуын бақылау.\n\n• **Сигнал қуаты (dBm):** Жоғары пиктер күштірек сигналды білдіреді (-30 dBm-ден -90 dBm-ге дейін).\n• **Арна ені:** Парабола табанының ені өткізу жолағын көрсетеді (20, 40, 80 немесе 160 МГц).\n• **Белсенді қосылым:** Графикте **қызыл түспен** ерекшеленіп, атауы қызыл тіктөртбұрыш ішінде көрсетіледі."
                "lt" -> "**Esmė:**\nRadijo bangų „Wi-Fi“ spektro ir užimtumo kanalų realaus laiko analizė.\n\n• **Signalas (dBm):** Aukštesni pikai rodo stipresnį ryšį (nuo -30 dBm iki -90 dBm).\n• **Kanalas:** Parabolės plotis nusako kanalo pralaidumą (20, 40, 80 ar 160 MHz).\n• **Aktyvus ryšys:** Grafike išskiriama **raudona** spalva, o pavadinimas rodomas raudoname rėmelyje."
                "lv" -> "**Būtība:**\nReāllaika Wi-Fi radiofrekvenču spektra un kanālu noslodzes monitorings.\n\n• **Signāla jauda (dBm):** Augstākas līknes norāda uz labāku uztveršanu (-30 dBm līdz -90 dBm).\n• **Kanāla platums:** Parabolas platums atbilst joslas platumam (20, 40, 80 vai 160 MHz).\n• **Aktīvais savienojums:** Iekrāsojas **sarkanā** krāsā ar nosaukumu sarkanā taisnstūrī."
                "sv" -> "**Koncept:**\nRealtidsanalys av Wi-Fi-spektrum och kanalfördelning.\n\n• **Signalstyrka (dBm):** Högre parabeltoppar anger starkare signaler (-30 dBm till -90 dBm).\n• **Kanalbredd:** Parabelns bredd anger bandbredden (20, 40, 80 eller 160 MHz).\n• **Aktiv anslutning:** Markeras i **rött** på grafen, med SSID i en röd rektangulär box."
                else -> "**Concept:**\nReal-time monitoring of Wi-Fi radio environment and wireless network spectrum allocation.\n\n• **Signal Strength (dBm):** Higher peaks indicate stronger signals (-30 dBm for excellent to -90 dBm for very poor reception).\n• **Channel Width:** The base width of the parabola corresponds to the network channel bandwidth (20, 40, 80, or 160 MHz).\n• **Active Connection:** The network currently connected is highlighted in **red** on the chart, and its SSID is enclosed in a red rectangular card for instant recognition."
            }
            else -> ""
        }
    }

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
}
