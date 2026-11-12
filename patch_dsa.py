with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Add states
content = content.replace(
'''    var errorMsg by remember { mutableStateOf<String?>(null) }''',
'''    var errorMsg by remember { mutableStateOf<String?>(null) }
    var showDsaWarning by remember { mutableStateOf(false) }
    var dsaWarningShownFor by remember { mutableStateOf<String?>(null) }'''
)

# 2. Add LaunchedEffect update
content = content.replace(
'''            parsedKeyType = kp.keyType
            isEncrypted = kp.isEncrypted
            isDecrypted = !kp.isEncrypted
            errorMsg = null''',
'''            parsedKeyType = kp.keyType
            isEncrypted = kp.isEncrypted
            isDecrypted = !kp.isEncrypted
            errorMsg = null
            
            val currentKeyString = String(bytes)
            if (kp.keyType == KeyPair.DSA && dsaWarningShownFor != currentKeyString) {
                showDsaWarning = true
                dsaWarningShownFor = currentKeyString
            }'''
)

# 3. Disable save button
content = content.replace(
'''enabled = !isSaving && isDecrypted && alias.isNotBlank() && parsedKeyType != null && (parsedKeyType != KeyPair.ED25519 || !isEncrypted)''',
'''enabled = !isSaving && isDecrypted && alias.isNotBlank() && parsedKeyType != null && (parsedKeyType != KeyPair.ED25519 || !isEncrypted) && parsedKeyType != KeyPair.DSA'''
)

# 4. Add AlertDialog
content = content.replace(
'''                            Text(translateText("Сохранить", context))
                        }
                    }
                }
            }
        }
    }
}''',
'''                            Text(translateText("Сохранить", context))
                        }
                    }
                }
            }
        }
        if (showDsaWarning) {
            AlertDialog(
                onDismissRequest = { showDsaWarning = false },
                title = { Text(translateText("Внимание", context)) },
                text = { Text(translateText("DSA-ключи не поддерживаются современными версиями SSH-сервера на OpenWrt - рекомендуется ED25519, RSA или ECDSA", context)) },
                confirmButton = {
                    TextButton(onClick = { showDsaWarning = false }) {
                        Text(translateText("Понятно", context))
                    }
                }
            )
        }
    }
}'''
)

with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'w', encoding='utf-8') as f:
    f.write(content)

print("Dialog patched")

# 5. Add translations
with open('app/src/main/java/com/example/MainActivity.kt', 'r', encoding='utf-8') as f:
    main_content = f.read()

translation_block = """        "DSA-ключи не поддерживаются современными версиями SSH-сервера на OpenWrt - рекомендуется ED25519, RSA или ECDSA" -> when (lang) {
            "ru" -> "DSA-ключи не поддерживаются современными версиями SSH-сервера на OpenWrt - рекомендуется ED25519, RSA или ECDSA"
            "uk" -> "DSA-ключі не підтримуються сучасними версіями SSH-сервера на OpenWrt - рекомендується ED25519, RSA або ECDSA"
            "be" -> "DSA-ключы не падтрымліваюцца сучаснымі версіямі SSH-сервера на OpenWrt - рэкамендуецца ED25519, RSA або ECDSA"
            "de" -> "DSA-Schlüssel werden von modernen SSH-Servern auf OpenWrt nicht unterstützt - ED25519, RSA oder ECDSA wird empfohlen"
            "es" -> "Las claves DSA no son compatibles con las versiones modernas del servidor SSH en OpenWrt: se recomienda ED25519, RSA o ECDSA"
            "fr" -> "Les clés DSA ne sont pas prises en charge par les versions modernes du serveur SSH sur OpenWrt - ED25519, RSA ou ECDSA est recommandé"
            "it" -> "Le chiavi DSA non sono supportate dalle versioni moderne del server SSH su OpenWrt: si consiglia ED25519, RSA o ECDSA"
            "pt" -> "Chaves DSA não são suportadas por versões modernas do servidor SSH no OpenWrt - ED25519, RSA ou ECDSA é recomendado"
            "da" -> "DSA-nøgler understøttes ikke af moderne SSH-serverversioner på OpenWrt - ED25519, RSA eller ECDSA anbefales"
            "fi" -> "Nykyaikaiset SSH-palvelimet OpenWrt:ssä eivät tue DSA-avaimia - suositellaan ED25519, RSA tai ECDSA"
            "kk" -> "DSA кілттері OpenWrt-дегі SSH серверінің заманауи нұсқаларымен қолданылмайды - ED25519, RSA немесе ECDSA ұсынылады"
            "lt" -> "DSA raktai nepalaikomi šiuolaikinėse SSH serverio versijose OpenWrt - rekomenduojama ED25519, RSA arba ECDSA"
            "lv" -> "DSA atslēgas netiek atbalstītas modernajās SSH servera versijās uz OpenWrt - ieteicams ED25519, RSA vai ECDSA"
            "ro" -> "Cheile DSA nu sunt acceptate de versiunile moderne ale serverului SSH de pe OpenWrt - se recomandă ED25519, RSA sau ECDSA"
            "sv" -> "DSA-nycklar stöds inte av moderna versioner av SSH-servern på OpenWrt - ED25519, RSA eller ECDSA rekommenderas"
            "bg" -> "DSA ключовете не се поддържат от съвременните версии на SSH сървъра на OpenWrt - препоръчват се ED25519, RSA или ECDSA"
            "cs" -> "Klíče DSA nejsou podporovány modernými verzemi SSH serveru na OpenWrt - doporučuje se ED25519, RSA nebo ECDSA"
            "el" -> "Τα κλειδιά DSA δεν υποστηρίζονται από τις σύγχρονες εκδόσεις του διακομιστή SSH στο OpenWrt - συνιστάται ED25519, RSA ή ECDSA"
            "et" -> "DSA-võtmeid ei toeta OpenWrt kaasaegsed SSH-serveri versioonid - soovitatav on ED25519, RSA või ECDSA"
            "hu" -> "A DSA kulcsokat nem támogatják a modern SSH szerver verziók az OpenWrt-n - ED25519, RSA vagy ECDSA ajánlott"
            "pl" -> "Klucze DSA nie są obsługiwane przez nowoczesne wersje serwera SSH w systemie OpenWrt - zalecane jest ED25519, RSA lub ECDSA"
            "sk" -> "Kľúče DSA nie sú podporované modernými verziami SSH servera na OpenWrt - odporúča sa ED25519, RSA alebo ECDSA"
            "sl" -> "Ključi DSA niso podprti v sodobnih različicah strežnika SSH na OpenWrt - priporoča se ED25519, RSA ali ECDSA"
            "tr" -> "DSA anahtarları OpenWrt'deki modern SSH sunucu sürümleri tarafından desteklenmemektedir - ED25519, RSA veya ECDSA önerilir"
            else -> "DSA keys are not supported by modern versions of the SSH server on OpenWrt - ED25519, RSA, or ECDSA is recommended"
        }
        "Понятно" -> when (lang) {
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
            "ro" -> "Am înțeles"
            "sv" -> "Förstått"
            "bg" -> "Разбрах"
            "cs" -> "Rozumím"
            "el" -> "Κατανοητό"
            "et" -> "Selge"
            "hu" -> "Értem"
            "pl" -> "Rozumiem"
            "sk" -> "Rozumiem"
            "sl" -> "Razumem"
            "tr" -> "Anladım"
            else -> "Got it"
        }
        "Внимание" -> when (lang) {
            "ru" -> "Внимание"
            "uk" -> "Увага"
            "be" -> "Увага"
            "de" -> "Achtung"
            "es" -> "Atención"
            "fr" -> "Attention"
            "it" -> "Attenzione"
            "pt" -> "Atenção"
            "da" -> "Opmærksomhed"
            "fi" -> "Huomio"
            "kk" -> "Назар аударыңыз"
            "lt" -> "Dėmesio"
            "lv" -> "Uzmanību"
            "ro" -> "Atenție"
            "sv" -> "Uppmärksamhet"
            "bg" -> "Внимание"
            "cs" -> "Pozor"
            "el" -> "Προσοχή"
            "et" -> "Tähelepanu"
            "hu" -> "Figyelem"
            "pl" -> "Uwaga"
            "sk" -> "Pozor"
            "sl" -> "Pozor"
            "tr" -> "Dikkat"
            else -> "Attention"
        }
"""

if '"Понятно"' not in main_content:
    insert_idx = main_content.find('"Пароль ключа" -> when (lang) {')
    if insert_idx != -1:
        end_idx = main_content.find('}', insert_idx) + 1
        end_idx = main_content.find('\n', end_idx) + 1
        main_content = main_content[:end_idx] + translation_block + main_content[end_idx:]
    
    with open('app/src/main/java/com/example/MainActivity.kt', 'w', encoding='utf-8') as f:
        f.write(main_content)
    print("Translations patched")

