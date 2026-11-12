with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'r', encoding='utf-8') as f:
    content = f.read()

replacement = """        val resetState = {
            inputMode = 1
            privateKeyText = ""
            privateKeyBytes = null
            fileName = null
            alias = ""
            passphrase = ""
            passwordVisible = false
            publicKeyText = ""
            parsedKeyType = null
            isEncrypted = false
            isDecrypted = true
            errorMsg = null
            dsaWarningShownFor = null
            showDsaWarning = false
        }
        
        if (showDsaWarning) {
            AlertDialog(
                onDismissRequest = { resetState() },
                title = { Text(translateText("Внимание", context)) },
                text = { Text(translateText("DSA-ключи не поддерживаются современными версиями SSH-сервера на OpenWrt - рекомендуется ED25519, RSA или ECDSA", context)) },
                confirmButton = {
                    TextButton(onClick = { resetState() }) {
                        Text(translateText("Понятно", context))
                    }
                }
            )
        }"""

content = content.replace(
'''        if (showDsaWarning) {
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
        }''', replacement)

with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'w', encoding='utf-8') as f:
    f.write(content)

print("Done")
