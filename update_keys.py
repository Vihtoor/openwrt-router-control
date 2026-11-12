with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
'''                        OutlinedTextField(
                            value = privateKeyText,
                            onValueChange = { privateKeyText = it },
                            label = { Text(translateText("Приватный ключ", context)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                            shape = RoundedCornerShape(12.dp)
                        )''',
'''                        OutlinedTextField(
                            value = privateKeyText,
                            onValueChange = { privateKeyText = it },
                            label = { Text(translateText("Приватный ключ", context)) },
                            placeholder = { Text(translateText("Вставьте сюда текст ключа", context)) },
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                            shape = RoundedCornerShape(12.dp)
                        )'''
)

content = content.replace(
'''                    OutlinedTextField(
                        value = publicKeyText,
                        onValueChange = { publicKeyText = it },
                        label = { Text(translateText("Публичный ключ", context)) },
                        placeholder = { Text("ssh-rsa AAAA...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                        shape = RoundedCornerShape(12.dp)
                    )''',
'''                    OutlinedTextField(
                        value = publicKeyText,
                        onValueChange = { publicKeyText = it },
                        label = { Text(translateText("Публичный ключ", context)) },
                        placeholder = { Text(translateText("Вставьте сюда текст ключа", context)) },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                        shape = RoundedCornerShape(12.dp)
                    )'''
)

with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'w', encoding='utf-8') as f:
    f.write(content)

# Now update MainActivity.kt translations
with open('app/src/main/java/com/example/MainActivity.kt', 'r', encoding='utf-8') as f:
    main_content = f.read()

translation_block = """        "Вставьте сюда текст ключа" -> when (lang) {
            "ru" -> "Вставьте сюда текст ключа"
            "uk" -> "Вставте сюди текст ключа"
            "be" -> "Устаўце сюды тэкст ключа"
            "de" -> "Schlüsseltext hier einfügen"
            "es" -> "Pegue el texto de la clave aquí"
            "fr" -> "Collez le texte de la clé ici"
            "it" -> "Incolla qui il testo della chiave"
            "pt" -> "Cole o texto da chave aqui"
            "da" -> "Indsæt nøgle tekst her"
            "fi" -> "Liitä avaimen teksti tähän"
            "kk" -> "Кілт мәтінін осыған қойыңыз"
            "lt" -> "Įklijuokite rakto tekstą čia"
            "lv" -> "Ielīmējiet atslēgas tekstu šeit"
            "ro" -> "Lipiți textul cheii aici"
            "sv" -> "Klistra in nyckeltext här"
            "bg" -> "Поставете текста на ключа тук"
            "cs" -> "Vložte sem text klíče"
            "el" -> "Επικολλήστε το κείμενο του κλειδιού εδώ"
            "et" -> "Kleepige võtme tekst siia"
            "hu" -> "Illessze be a kulcs szövegét ide"
            "pl" -> "Wklej tekst klucza tutaj"
            "sk" -> "Sem vložte text kľúča"
            "sl" -> "Tukaj prilepite besedilo ključa"
            "tr" -> "Anahtar metnini buraya yapıştırın"
            else -> "Paste key text here"
        }
"""

if '"Вставьте сюда текст ключа"' not in main_content:
    # Find a good place to insert, e.g. after "Пароль ключа" -> when (lang) { block
    insert_idx = main_content.find('"Пароль ключа" -> when (lang) {')
    if insert_idx != -1:
        # Find the end of this block
        end_idx = main_content.find('}', insert_idx) + 1
        # Insert after the newline
        end_idx = main_content.find('\n', end_idx) + 1
        main_content = main_content[:end_idx] + translation_block + main_content[end_idx:]
    
    with open('app/src/main/java/com/example/MainActivity.kt', 'w', encoding='utf-8') as f:
        f.write(main_content)

print("Done")
