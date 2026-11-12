with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'r', encoding='utf-8') as f:
    content = f.read()

import re

# Patch Private Key
content = content.replace(
    '''                        OutlinedTextField(
                            value = privateKeyText,
                            onValueChange = { privateKeyText = it },
                            label = { Text(translateText("Приватный ключ", context)) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 200.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                            shape = RoundedCornerShape(12.dp)
                        )''',
    '''                        OutlinedTextField(
                            value = privateKeyText,
                            onValueChange = { privateKeyText = it },
                            label = { Text(translateText("Приватный ключ", context)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                            shape = RoundedCornerShape(12.dp)
                        )'''
)

# Patch Public Key
content = content.replace(
    '''                    OutlinedTextField(
                        value = publicKeyText,
                        onValueChange = { publicKeyText = it },
                        label = { Text(translateText("Публичный ключ", context)) },
                        placeholder = { Text("ssh-rsa AAAA...") },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                        shape = RoundedCornerShape(12.dp)
                    )''',
    '''                    OutlinedTextField(
                        value = publicKeyText,
                        onValueChange = { publicKeyText = it },
                        label = { Text(translateText("Публичный ключ", context)) },
                        placeholder = { Text("ssh-rsa AAAA...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                        shape = RoundedCornerShape(12.dp)
                    )'''
)

with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'w', encoding='utf-8') as f:
    f.write(content)

print("Done")
