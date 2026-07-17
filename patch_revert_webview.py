import re

file_path = "app/src/main/java/com/example/MainActivity.kt"
with open(file_path, "r") as f:
    content = f.read()

bad_target = """                update = { webView ->
                    if (webView.url != speedTestUrl) {
                        webView.loadUrl(speedTestUrl)
                    }
                },
                modifier = Modifier.fillMaxSize().focusRequester(terminalWindowFocusRequester).focusProperties {
                    up = clearButtonFocusRequester
                    down = termKeysFocusRequester
                }
            )"""

good_replacement = """                update = { webView ->
                    if (webView.url != speedTestUrl) {
                        webView.loadUrl(speedTestUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )"""

content = content.replace(bad_target, good_replacement)

with open(file_path, "w") as f:
    f.write(content)

print("Reverted WebView modification")
