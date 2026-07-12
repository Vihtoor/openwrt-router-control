with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """            }

            // Wi-Fi Analyzer Card"""

replacement = """            }

            val locale = context.resources.configuration.locales[0].language

            // Wi-Fi Analyzer Card"""

content = content.replace(target, replacement)

# Remove the old locale definition
target2 = """            // Beautiful M3 iPerf3 Card below M-Lab
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(iperfCardFocusRequester)
                    .onFocusChanged { isIperfFocused = it.isFocused }"""

# Wait, the old one is at:
# """
#
#            val locale = context.resources.configuration.locales[0].language
#
#            // Beautiful M3 Header Card
# """
target3 = """            val locale = context.resources.configuration.locales[0].language

            // Beautiful M3 Header Card"""
replacement3 = """            // Beautiful M3 Header Card"""
content = content.replace(target3, replacement3)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
