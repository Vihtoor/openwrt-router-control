import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# 1. LaunchedEffect
content = content.replace("firstCardFocusRequester.requestFocus()", "devicesCardFocusRequester.requestFocus()", 1)

# 2. In Devices card
# It currently has:
#                                             Key.DirectionUp -> {
#                                                 iperfCardFocusRequester.requestFocus()
#                                                 true
#                                             }
content = re.sub(r'Key\.DirectionUp -> \{\s*iperfCardFocusRequester\.requestFocus\(\)\s*true\s*\}', r'''Key.DirectionDown -> {
                                                firstCardFocusRequester.requestFocus()
                                                true
                                            }''', content, count=1)

content = re.sub(r'Key\.DirectionUp -> \{\s*iperfHelpFocusRequester\.requestFocus\(\)\s*true\s*\}', r'''Key.DirectionDown -> {
                                                        mlabHelpFocusRequester.requestFocus()
                                                        true
                                                    }''', content, count=1)

# 3. First card (M-Lab) Up Arrow
# In firstCardFocusRequester block:
#                                             Key.DirectionDown -> {
#                                                 cloudflareCardFocusRequester.requestFocus()
#                                                 true
#                                             }
mlab_card_up = """                                            Key.DirectionUp -> {
                                                devicesCardFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {"""
content = content.replace("Key.DirectionDown -> {\n                                                cloudflareCardFocusRequester.requestFocus()", mlab_card_up + "\n                                                cloudflareCardFocusRequester.requestFocus()", 1)

mlab_help_up = """                                                    Key.DirectionUp -> {
                                                        devicesHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {"""
content = content.replace("Key.DirectionDown -> {\n                                                        cloudflareHelpFocusRequester.requestFocus()", mlab_help_up + "\n                                                        cloudflareHelpFocusRequester.requestFocus()", 1)

# 4. Iperf card Down Arrow
# iperfCard currently has:
#                                             Key.DirectionDown -> {
#                                                 devicesCardFocusRequester.requestFocus()
#                                                 true
#                                             }
content = re.sub(r'Key\.DirectionDown -> \{\s*devicesCardFocusRequester\.requestFocus\(\)\s*true\s*\}', '', content, count=1)

content = re.sub(r'Key\.DirectionDown -> \{\s*devicesHelpFocusRequester\.requestFocus\(\)\s*true\s*\}', '', content, count=1)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
print("done")
