import urllib.request
import urllib.parse
import json
import time
import sys
from concurrent.futures import ThreadPoolExecutor

strings = [
    "ED25519 ключи с паролем не поддерживаются библиотекой JSch. Вы не сможете использовать этот ключ.",
    "Алгоритм",
    "Вход по SSH ключу",
    "Вход по паролю",
    "Генерация SSH-ключа",
    "Генерация ключей производится в ОЗУ. Приватный ключ будет надежно зашифрован.",
    "Для большей безопасности рекомендуем позже переключиться на вход по ключу",
    "Закрыть",
    "Запомнить пароль на устройстве",
    "Из файла",
    "Импорт",
    "Импорт ключа",
    "Имя ключа",
    "Ключ для этого роутера",
    "Кодовая фраза ключа (если есть)",
    "Название ключа",
    "Например: домашний ноутбук",
    "Неверный пароль",
    "Нет сохраненных ключей",
    "Новый ключ",
    "Опции",
    "Пароль",
    "Пароль для ключа (опционально)",
    "Пароль ключа",
    "Пароль подходит",
    "Поделиться",
    "Поделиться публичным ключом",
    "После добавления ключа зайдите на роутер в браузере или терминале по паролю, чтобы добавить его",
    "Приватный ключ",
    "Примечание: для ED25519 пароль ключа не поддерживается",
    "Публичная часть не найдена — пожалуйста, вставьте её вручную",
    "Публичный ключ",
    "Публичный ключ скопирован",
    "Публичный ключ (скопируйте на роутер)",
    "Сгенерировать",
    "Скопировать",
    "Сохранить",
    "Способ входа",
    "Текстом",
    "Управление SSH-ключами",
    "Файл публичного ключа не найден",
    "Формат распознан"
]

langs = ["uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "ro", "sv", "bg", "cs", "el", "et", "hu", "pl", "sk", "sl", "tr", "en"]

def translate_one(text, target):
    url = f"https://translate.googleapis.com/translate_a/single?client=gtx&sl=ru&tl={target}&dt=t&q={urllib.parse.quote(text)}"
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
    try:
        response = urllib.request.urlopen(req)
        data = json.loads(response.read().decode('utf-8'))
        return "".join([d[0] for d in data[0] if d[0]])
    except Exception as e:
        return text

def translate_all(text):
    res = {}
    with ThreadPoolExecutor(max_workers=5) as executor:
        futures = {executor.submit(translate_one, text, l): l for l in langs}
        for future in futures:
            l = futures[future]
            res[l] = future.result()
    return res

with open("translations_block.kt", "w") as f:
    for i, s in enumerate(strings):
        print(f"Translating {i+1}/{len(strings)}: {s}")
        f.write(f'        "{s}" -> when (lang) {{\n')
        f.write(f'            "ru" -> "{s}"\n')
        res = translate_all(s)
        for l in langs:
            trans = res[l].replace('"', '\\"')
            if l == "en":
                f.write(f'            else -> "{trans}"\n')
            else:
                f.write(f'            "{l}" -> "{trans}"\n')
        f.write('        }\n')
        f.flush()

print("Done translating!")
