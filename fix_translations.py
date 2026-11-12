import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r', encoding='utf-8') as f:
    content = f.read()

replacements = {
    '"Публичный ключ (скопируйте на роутер)" -> when (lang) {': '"Публичный ключ (добавьте на роутер)" -> when (lang) {',
    '"ru" -> "Публичный ключ (скопируйте на роутер)"': '"ru" -> "Публичный ключ (добавьте на роутер)"',
    '"uk" -> "Публічний ключ (скопіюйте на роутер)"': '"uk" -> "Публічний ключ (додайте на роутер)"',
    '"be" -> "Публічны ключ (скапіюйце на роўтэр)"': '"be" -> "Публічны ключ (дадайце на роўтэр)"',
    '"de" -> "Öffentlicher Schlüssel (auf Router kopieren)"': '"de" -> "Öffentlicher Schlüssel (auf Router hinzufügen)"',
    '"es" -> "Clave pública (copiar al enrutador)"': '"es" -> "Clave pública (agregar al enrutador)"',
    '"fr" -> "Clé publique (copie sur le routeur)"': '"fr" -> "Clé publique (ajouter au routeur)"',
    '"it" -> "Chiave pubblica (copia sul router)"': '"it" -> "Chiave pubblica (aggiungi al router)"',
    '"pt" -> "Chave pública (cópia para roteador)"': '"pt" -> "Chave pública (adicionar ao roteador)"',
    '"da" -> "Offentlig nøgle (kopi til router)"': '"da" -> "Offentlig nøgle (tilføj til router)"',
    '"fi" -> "Julkinen avain (kopioi reitittimeen)"': '"fi" -> "Julkinen avain (lisää reitittimeen)"',
    '"kk" -> "Ашық кілт (маршрутизаторға көшіру)"': '"kk" -> "Ашық кілт (маршрутизаторға қосу)"',
    '"lt" -> "Viešasis raktas (kopijuoti į maršrutizatorių)"': '"lt" -> "Viešasis raktas (pridėti prie maršrutizatoriaus)"',
    '"lv" -> "Publiskā atslēga (kopēt maršrutētājā)"': '"lv" -> "Publiskā atslēga (pievienot maršrutētājam)"',
    '"ro" -> "Cheie publică (copiere pe router)"': '"ro" -> "Cheie publică (adăugați pe router)"',
    '"sv" -> "Offentlig nyckel (kopiera till router)"': '"sv" -> "Offentlig nyckel (lägg till i router)"',
    '"bg" -> "Публичен ключ (копиране в рутер)"': '"bg" -> "Публичен ключ (добавете в рутера)"',
    '"cs" -> "Veřejný klíč (zkopírujte do routeru)"': '"cs" -> "Veřejný klíč (přidejte do routeru)"',
    '"el" -> "Δημόσιο κλειδί (αντιγραφή στο δρομολογητή)"': '"el" -> "Δημόσιο κλειδί (προσθήκη στον δρομολογητή)"',
    '"et" -> "Avalik võti (kopeeri ruuterisse)"': '"et" -> "Avalik võti (lisa ruuterisse)"',
    '"hu" -> "Nyilvános kulcs (másolás útválasztóra)"': '"hu" -> "Nyilvános kulcs (hozzáadás az útválasztóhoz)"',
    '"pl" -> "Klucz publiczny (skopiuj do routera)"': '"pl" -> "Klucz publiczny (dodaj do routera)"',
    '"sk" -> "Verejný kľúč (skopírovať do smerovača)"': '"sk" -> "Verejný kľúč (pridať do smerovača)"',
    '"sl" -> "Javni ključ (kopiraj na usmerjevalnik)"': '"sl" -> "Javni ključ (dodaj na usmerjevalnik)"',
    '"tr" -> "Genel anahtar (yönlendiriciye kopyala)"': '"tr" -> "Genel anahtar (yönlendiriciye ekle)"',
    'else -> "Public key (copy to router)"': 'else -> "Public key (add to router)"'
}

for old, new in replacements.items():
    content = content.replace(old, new)

with open('app/src/main/java/com/example/MainActivity.kt', 'w', encoding='utf-8') as f:
    f.write(content)

with open('app/src/main/java/com/example/ui/SettingsPanel.kt', 'r', encoding='utf-8') as f:
    settings_content = f.read()

settings_content = settings_content.replace('"Публичный ключ (скопируйте на роутер)"', '"Публичный ключ (добавьте на роутер)"')

with open('app/src/main/java/com/example/ui/SettingsPanel.kt', 'w', encoding='utf-8') as f:
    f.write(settings_content)

print("Done")
