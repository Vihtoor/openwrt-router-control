import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    code = f.read()

# Replace the body of translateText
new_main_method = """fun translateText(raw: String, context: android.content.Context): String {
    val lang = context.resources.configuration.locales[0].language
    translateTextPart1(raw, lang)?.let { return it }
    translateTextPart2(raw, lang)?.let { return it }
    translateTextPart3(raw, lang)?.let { return it }
    translateTextPart4(raw, lang)?.let { return it }

    if (raw.startsWith("Запущена (") && raw.endsWith(")")) {
        val inst = raw.substringAfter("(").substringBefore(")")
        return context.getString(R.string.status_running_inst, inst)
    } else if (raw.startsWith("Интерфейс ") && raw.endsWith(" поднят")) {
        val iface = raw.substringAfter("Интерфейс ").substringBefore(" поднят")
        return context.getString(R.string.status_wg_up, iface)
    } else if (raw.endsWith(" Остановлена")) {
        val service = raw.substringBefore(" Остановлена")
        return context.getString(R.string.status_ovpn_stopped, service)
    } else if (raw.endsWith(" Запущена")) {
        val service = raw.substringBefore(" Запущена")
        return context.getString(R.string.status_ovpn_running, service)
    } else if (raw.endsWith(" Выключен")) {
        val iface = raw.substringBefore(" Выключен")
        return context.getString(R.string.status_wg_off, iface)
    } else {
        return raw
    }
}
"""

match = re.search(r"fun translateText\(raw: String, context: android\.content\.Context\): String \{\s+val lang = context\.resources\.configuration\.locales\[0\]\.language\s+translateTextPart1\(raw, lang\)\?\.let \{ return it \}.*?return when \{\s+if \(raw.*?raw\s+\}\s+\}", code, re.DOTALL)

if match:
    new_code = code[:match.start()] + new_main_method + code[match.end():]
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(new_code)
    print("Fixed!")
else:
    print("Not found!")

