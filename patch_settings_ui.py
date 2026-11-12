import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r") as f:
    content = f.read()

target = """            item {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            onFieldChanged()
                        },
                        label = { Text(translateText("Пароль или приватный ключ", context)) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                                                singleLine = true,
                        modifier = Modifier.weight(1f).onFocusChanged {
                            isPasswordFocused = it.isFocused
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(300)
                                    listState.animateScrollBy(10000f)
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    val image = if (passwordVisible) Icons.Default.Clear else Icons.Default.Lock
                    val iconInteractionSource = remember { MutableInteractionSource() }
                    val isIconFocused by iconInteractionSource.collectIsFocusedAsState()
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        interactionSource = iconInteractionSource,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .background(if (isIconFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                    ) {
                        Icon(
                            imageVector = image, 
                            contentDescription = null,
                            tint = if (isIconFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                        )
                    }
                }
            }"""

replacement = """            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = { 
                                password = it
                                onFieldChanged()
                            },
                            label = { Text(if (sshKeyId != null) translateText("Пароль для SSH-ключа", context) else translateText("Пароль", context)) },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            singleLine = true,
                            modifier = Modifier.weight(1f).onFocusChanged {
                                isPasswordFocused = it.isFocused
                                if (it.isFocused) {
                                    coroutineScope.launch {
                                        kotlinx.coroutines.delay(300)
                                        listState.animateScrollBy(10000f)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        val image = if (passwordVisible) Icons.Default.Clear else Icons.Default.Lock
                        val iconInteractionSource = remember { MutableInteractionSource() }
                        val isIconFocused by iconInteractionSource.collectIsFocusedAsState()
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            interactionSource = iconInteractionSource,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .background(if (isIconFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                        ) {
                            Icon(
                                imageVector = image, 
                                contentDescription = null,
                                tint = if (isIconFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    var showSshKeyDialog by remember { mutableStateOf(false) }
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = if (sshKeyId != null) translateText("Выбран SSH-ключ", context) else translateText("Вход по паролю", context),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = { showSshKeyDialog = true }) {
                            Text(translateText("Настроить SSH-ключи", context))
                        }
                    }
                    if (showSshKeyDialog) {
                        com.example.ui.SshKeyManagerDialog(
                            onDismiss = { showSshKeyDialog = false },
                            onSelectKey = { id -> 
                                sshKeyId = id
                                onFieldChanged()
                                showSshKeyDialog = false
                            },
                            selectedKeyId = sshKeyId
                        )
                    }
                }
            }"""

if target in content:
    content = content.replace(target, replacement)
    print("Patched UI successfully")
else:
    print("UI target not found")

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w") as f:
    f.write(content)
