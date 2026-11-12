import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    code = f.read()

# Find the translateText method
match = re.search(r"fun translateText\(raw: String, context: android\.content\.Context\): String \{\s+val lang = context\.resources\.configuration\.locales\[0\]\.language\s+return when \(raw\) \{(.*?)^\s+\}\s+else -> \{(.*?)\}\s+\}\s+\}", code, re.DOTALL | re.MULTILINE)

if not match:
    print("Could not find translateText!")
    exit(1)

cases_block = match.group(1)
else_block = match.group(2)

# cases_block contains a lot of `"str" -> when(lang) { ... }`
# Let's split it into parts of ~20 cases each
cases = re.findall(r'(\s*"[^"]+" -> when \(lang\) \{.*?\n\s+\})', cases_block, re.DOTALL)

print(f"Found {len(cases)} cases.")

part_size = 20
parts = []
for i in range(0, len(cases), part_size):
    parts.append(cases[i:i+part_size])

new_methods = ""
for i, part in enumerate(parts):
    new_methods += f"fun translateTextPart{i+1}(raw: String, lang: String): String? {{\n"
    new_methods += "    return when (raw) {\n"
    new_methods += "".join(part)
    new_methods += "\n        else -> null\n"
    new_methods += "    }\n"
    new_methods += "}\n\n"

new_main_method = """fun translateText(raw: String, context: android.content.Context): String {
    val lang = context.resources.configuration.locales[0].language
"""
for i in range(len(parts)):
    new_main_method += f"    translateTextPart{i+1}(raw, lang)?.let {{ return it }}\n"

new_main_method += """
    return when {
""" + else_block + """
    }
}
"""

new_code = code[:match.start()] + new_main_method + "\n" + new_methods + code[match.end():]

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(new_code)

print("Split successfully!")
