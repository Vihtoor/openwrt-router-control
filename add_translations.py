with open('app/src/main/java/com/example/MainActivity.kt', 'r', encoding='utf-8') as f:
    main_content = f.read()

translation_block = """        "Показать пароль" -> when (lang) {
            "ru" -> "Показать пароль"
            "uk" -> "Показати пароль"
            "be" -> "Паказаць пароль"
            "de" -> "Passwort anzeigen"
            "es" -> "Mostrar contraseña"
            "fr" -> "Afficher le mot de passe"
            "it" -> "Mostra password"
            "pt" -> "Mostrar senha"
            "da" -> "Vis adgangskode"
            "fi" -> "Näytä salasana"
            "kk" -> "Құпиясөзді көрсету"
            "lt" -> "Rodyti slaptažodį"
            "lv" -> "Rādīt paroli"
            "ro" -> "Arată parola"
            "sv" -> "Visa lösenord"
            "bg" -> "Покажи паролата"
            "cs" -> "Zobrazit heslo"
            "el" -> "Εμφάνιση κωδικού"
            "et" -> "Näita parooli"
            "hu" -> "Jelszó megjelenítése"
            "pl" -> "Pokaż hasło"
            "sk" -> "Zobraziť heslo"
            "sl" -> "Prikaži geslo"
            "tr" -> "Şifreyi göster"
            else -> "Show password"
        }
        "Скрыть пароль" -> when (lang) {
            "ru" -> "Скрыть пароль"
            "uk" -> "Приховати пароль"
            "be" -> "Схаваць пароль"
            "de" -> "Passwort verbergen"
            "es" -> "Ocultar contraseña"
            "fr" -> "Masquer le mot de passe"
            "it" -> "Nascondi password"
            "pt" -> "Ocultar senha"
            "da" -> "Skjul adgangskode"
            "fi" -> "Piilota salasana"
            "kk" -> "Құпиясөзді жасыру"
            "lt" -> "Slėpti slaptažodį"
            "lv" -> "Paslēpt paroli"
            "ro" -> "Ascunde parola"
            "sv" -> "Dölj lösenord"
            "bg" -> "Скрий паролата"
            "cs" -> "Skrýt heslo"
            "el" -> "Απόκρυψη κωδικού"
            "et" -> "Peida parool"
            "hu" -> "Jelszó elrejtése"
            "pl" -> "Ukryj hasło"
            "sk" -> "Skryť heslo"
            "sl" -> "Skrij geslo"
            "tr" -> "Şifreyi gizle"
            else -> "Hide password"
        }
"""

if '"Показать пароль"' not in main_content:
    insert_idx = main_content.find('"Пароль ключа" -> when (lang) {')
    if insert_idx != -1:
        end_idx = main_content.find('}', insert_idx) + 1
        end_idx = main_content.find('\n', end_idx) + 1
        main_content = main_content[:end_idx] + translation_block + main_content[end_idx:]
    
    with open('app/src/main/java/com/example/MainActivity.kt', 'w', encoding='utf-8') as f:
        f.write(main_content)

print("Done")
