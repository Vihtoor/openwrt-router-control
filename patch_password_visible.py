with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Add state variable
content = content.replace(
'''    var passphrase by remember { mutableStateOf("") }
    var publicKeyText by remember { mutableStateOf("") }''',
'''    var passphrase by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var publicKeyText by remember { mutableStateOf("") }'''
)

# 2. Update the password field
content = content.replace(
'''                            OutlinedTextField(
                                value = passphrase,
                                onValueChange = { passphrase = it },
                                label = { Text(translateText("Пароль ключа", context)) },
                                singleLine = true,
                                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )''',
'''                            OutlinedTextField(
                                value = passphrase,
                                onValueChange = { passphrase = it },
                                label = { Text(translateText("Пароль ключа", context)) },
                                singleLine = true,
                                visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password),
                                trailingIcon = {
                                    val image = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                                    val description = if (passwordVisible) "Скрыть пароль" else "Показать пароль"
                                    
                                    val isTv = context.packageManager.hasSystemFeature("android.software.leanback")
                                    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                    val isFocused by interactionSource.collectIsFocusedAsState()
                                    
                                    IconButton(
                                        onClick = { passwordVisible = !passwordVisible },
                                        interactionSource = interactionSource,
                                        modifier = Modifier.background(if (isTv && isFocused) MaterialTheme.colorScheme.surfaceVariant else androidx.compose.ui.graphics.Color.Transparent, androidx.compose.foundation.shape.CircleShape)
                                    ) {
                                        Icon(imageVector = image, contentDescription = description)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )'''
)

with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'w', encoding='utf-8') as f:
    f.write(content)

print("Done")
